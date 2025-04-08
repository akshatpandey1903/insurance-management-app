package com.aurionpro.app.service;

import com.aurionpro.app.dto.CustomerPolicyRequestDTO;
import com.aurionpro.app.dto.CustomerPolicyResponseDTO;

public interface CustomerPolicyService {
	//void verifyAndActivatePolicy(RazorpayPaymentResponseDTO dto);
	CustomerPolicyResponseDTO registerPolicy(CustomerPolicyRequestDTO dto, int id);
	CustomerPolicyResponseDTO approveCustomerPolicy(int policyId, int employeeId);
}
