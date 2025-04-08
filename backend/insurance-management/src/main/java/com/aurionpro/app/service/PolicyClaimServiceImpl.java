package com.aurionpro.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.PolicyClaimRequestDTO;
import com.aurionpro.app.dto.PolicyClaimResponseDTO;
import com.aurionpro.app.entity.CustomerPolicy;
import com.aurionpro.app.entity.PolicyClaim;
import com.aurionpro.app.entity.WithdrawalStatus;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.CustomerPolicyRepository;
import com.aurionpro.app.repository.CustomerRepository;
import com.aurionpro.app.repository.PolicyClaimRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PolicyClaimServiceImpl implements PolicyClaimService {
	
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
}

