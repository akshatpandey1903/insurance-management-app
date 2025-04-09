package com.aurionpro.app.service;

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
import com.aurionpro.app.entity.WithdrawalStatus;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.CustomerPolicyRepository;
import com.aurionpro.app.repository.CustomerRepository;
import com.aurionpro.app.repository.EmployeeRepository;
import com.aurionpro.app.repository.PolicyClaimRepository;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyClaimServiceImpl implements PolicyClaimService {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
    private PolicyClaimRepository policyClaimRepository;
	
	@Autowired
    private CustomerPolicyRepository customerPolicyRepository;
	
	@Autowired
    private CustomerRepository customerRepository;

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

        PolicyClaim claim = new PolicyClaim();
        claim.setCustomerPolicy(policy);
        claim.setReason(request.getReason());
        claim.setStatus(WithdrawalStatus.PENDING);

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

