package com.aurionpro.app.service;

import java.util.List;

import com.aurionpro.app.dto.CustomerPolicyRequestDTO;
import com.aurionpro.app.dto.CustomerPolicyResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.PurchasedPolicyDto;

public interface CustomerPolicyService {
	//void verifyAndActivatePolicy(RazorpayPaymentResponseDTO dto);
	CustomerPolicyResponseDTO registerPolicy(CustomerPolicyRequestDTO dto, int id);
	CustomerPolicyResponseDTO approveCustomerPolicy(int policyId, int employeeId);
	CustomerPolicyResponseDTO cancelPolicy(int customerId, int policyId);
	PageResponse<CustomerPolicyResponseDTO> getUnapprovedPolicies(int page, int size);
	void rejectCustomerPolicy(int policyId, int employeeId, String reason);
	List<PurchasedPolicyDto> getPurchasedPolicies(int customerId);
}
