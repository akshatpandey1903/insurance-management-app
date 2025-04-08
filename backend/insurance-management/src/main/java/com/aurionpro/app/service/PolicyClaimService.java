package com.aurionpro.app.service;

import java.util.List;

import com.aurionpro.app.dto.PolicyClaimRequestDTO;
import com.aurionpro.app.dto.PolicyClaimResponseDTO;

public interface PolicyClaimService {
	PolicyClaimResponseDTO raiseClaim(PolicyClaimRequestDTO request, int customerId);
    List<PolicyClaimResponseDTO> getClaimsForCustomer(int customerId);
}
