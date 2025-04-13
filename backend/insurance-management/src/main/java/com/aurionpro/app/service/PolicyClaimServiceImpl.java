package com.aurionpro.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.ClaimApprovalRequestDTO;
import com.aurionpro.app.dto.ClaimFilterRequestDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.PolicyClaimRequestDTO;
import com.aurionpro.app.dto.PolicyClaimResponseDTO;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.CustomerPolicy;
import com.aurionpro.app.entity.Employee;
import com.aurionpro.app.entity.PolicyClaim;
import com.aurionpro.app.entity.Transaction;
import com.aurionpro.app.entity.TransactionType;
import com.aurionpro.app.entity.WithdrawalStatus;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.CustomerPolicyRepository;
import com.aurionpro.app.repository.EmployeeRepository;
import com.aurionpro.app.repository.PolicyClaimRepository;
import com.aurionpro.app.repository.TransactionRepository;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyClaimServiceImpl implements PolicyClaimService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
    private PolicyClaimRepository policyClaimRepository;
	
	@Autowired
    private CustomerPolicyRepository customerPolicyRepository;

	@Override
	@Transactional
	public PolicyClaimResponseDTO raiseClaim(PolicyClaimRequestDTO request, int customerId) {
	    CustomerPolicy policy = customerPolicyRepository.findById(request.getPolicyId())
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Policy not found"));

	    if (policy.getCustomer().getUserId() != customerId) {
	        throw new ResourceNotFoundException(HttpStatus.FORBIDDEN, "Not your policy");
	    }

	    if (!policy.isActive()) {
	        throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Policy is not active");
	    }

	    boolean alreadyClaimed = policyClaimRepository.existsByCustomerPolicyAndIsDeletedFalse(policy);
	    if (alreadyClaimed) {
	        throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Claim already exists for this policy");
	    }

	    LocalDate start = policy.getStartDate();
	    LocalDate end = policy.getEndDate();
	    LocalDate today = LocalDate.now();

	    long totalDays = ChronoUnit.DAYS.between(start, end);
	    long daysPassed = ChronoUnit.DAYS.between(start, today);

	    if (totalDays <= 0) {
	        throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Policy duration is invalid");
	    }

	    BigDecimal selectedCoverage = policy.getSelectedCoverageAmount();
	    BigDecimal penalty = BigDecimal.ZERO;
	    BigDecimal claimAmount;
	    boolean isEarly = false;

	    if (today.isBefore(end)) {
	        isEarly = true;
	        BigDecimal completionRatio = BigDecimal.valueOf(daysPassed)
	                .divide(BigDecimal.valueOf(totalDays), 4, RoundingMode.HALF_UP);

	        BigDecimal penaltyRatio = BigDecimal.ONE.subtract(completionRatio);
	        penalty = selectedCoverage.multiply(penaltyRatio).setScale(2, RoundingMode.HALF_UP);
	        claimAmount = selectedCoverage.subtract(penalty).max(BigDecimal.ZERO);
	    } else {
	        penalty = BigDecimal.ZERO;
	        claimAmount = selectedCoverage;
	    }
    
	    System.out.println("Penalty: " + penalty + ", Claim: " + claimAmount);
	    PolicyClaim claim = new PolicyClaim();
	    claim.setCustomerPolicy(policy);
	    claim.setReason(request.getReason());
	    claim.setStatus(WithdrawalStatus.PENDING);
	    claim.setEarlyClaim(isEarly);
	    claim.setPenaltyAmount(penalty);
	    claim.setClaimAmount(claimAmount != null ? claimAmount : BigDecimal.ZERO);

	    PolicyClaim savedClaim = policyClaimRepository.save(claim);
	    return toDTO(savedClaim);
	}


    @Override
    public List<PolicyClaimResponseDTO> getClaimsForCustomer(int customerId) {
        return policyClaimRepository.findByCustomerPolicy_Customer_UserIdAndIsDeletedFalse(customerId)
                .stream().map(this::toDTO).toList();
    }

    private PolicyClaimResponseDTO toDTO(PolicyClaim claim) {
        PolicyClaimResponseDTO dto = new PolicyClaimResponseDTO();
        dto.setId(claim.getId());
        dto.setPolicyId(claim.getCustomerPolicy().getId());
        dto.setPlanName(claim.getCustomerPolicy().getInsurancePlan().getPlanName());
        dto.setReason(claim.getReason());
        dto.setStatus(claim.getStatus().name());
        dto.setRemarks(claim.getRemarks());
        dto.setVerifiedBy(claim.getVerifiedBy() != null
                ? claim.getVerifiedBy().getFirstName() + " " + claim.getVerifiedBy().getLastName()
                : null);
        dto.setRequestedAt(claim.getRequestedAt());
        dto.setClaimAmount(claim.getClaimAmount());
        dto.setPenaltyAmount(claim.getPenaltyAmount());
        return dto;
    }
    
    @Override
    @Transactional
    public PolicyClaimResponseDTO approveClaim(ClaimApprovalRequestDTO requestDTO) {
        PolicyClaim claim = policyClaimRepository.findById(requestDTO.getClaimId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Claim not found"));

        if (claim.getStatus() != WithdrawalStatus.PENDING) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Claim already processed");
        }

        if (requestDTO.getStatus() != WithdrawalStatus.APPROVED && requestDTO.getStatus() != WithdrawalStatus.REJECTED) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Invalid status");
        }

        Employee employee = employeeRepository.findById(requestDTO.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Employee not found"));

        claim.setStatus(requestDTO.getStatus());
        claim.setRemarks(requestDTO.getRemarks());
        claim.setVerifiedBy(employee);

        policyClaimRepository.save(claim);
        
        CustomerPolicy policy = claim.getCustomerPolicy();
        
        if(requestDTO.getStatus() == WithdrawalStatus.APPROVED) {
        	Transaction txn = new Transaction();
            txn.setAmount(claim.getClaimAmount());
            txn.setDescription("Policy Claim payment to Customer");
            txn.setTransactionType(TransactionType.CLAIM);
            txn.setUser(policy.getCustomer());
            txn.setUserRole("CUSTOMER");
            txn.setCustomerPolicy(policy);

            transactionRepository.save(txn);
        }

        return toDTO(claim);
    }
    
    @Override
    public PageResponse<PolicyClaimResponseDTO> getAllClaims(ClaimFilterRequestDTO filter, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("requestedAt").descending());

        Specification<PolicyClaim> spec = Specification.where((root, query, cb) -> cb.isFalse(root.get("isDeleted")));

        if (filter.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getCustomerName() != null && !filter.getCustomerName().isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<PolicyClaim, CustomerPolicy> policyJoin = root.join("customerPolicy");
                Join<CustomerPolicy, Customer> customerJoin = policyJoin.join("customer");
                Expression<String> fullName = cb.concat(customerJoin.get("firstName"), cb.concat(" ", customerJoin.get("lastName")));
                return cb.like(cb.lower(fullName), "%" + filter.getCustomerName().toLowerCase() + "%");
            });
        }

        if (filter.getFromDate() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("requestedAt"), filter.getFromDate().atStartOfDay()));
        }

        if (filter.getToDate() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("requestedAt"), filter.getToDate().atTime(23, 59, 59)));
        }

        Page<PolicyClaim> page = policyClaimRepository.findAll(spec, pageable);

        List<PolicyClaimResponseDTO> dtos = page.getContent().stream()
            .map(this::toDTO)
            .toList();

        return new PageResponse<>(
        		dtos,
        		page.getTotalPages(),
        		page.getTotalElements(),
        		page.getSize(),
        		page.isLast()
        );
    }


}

