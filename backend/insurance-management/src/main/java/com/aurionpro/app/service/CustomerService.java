package com.aurionpro.app.service;

import java.util.List;

import com.aurionpro.app.dto.CustomerBasicDTO;
import com.aurionpro.app.dto.CustomerProfileDTO;
import com.aurionpro.app.dto.CustomerRegistrationDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.UserResponseDTO;

public interface CustomerService {
	CustomerProfileDTO viewProfile(int customerId);
	CustomerProfileDTO updateProfile(int customerId, CustomerProfileDTO dto);
	UserResponseDTO registerCustomerByAgent(CustomerRegistrationDTO dto, int agentId);
	PageResponse<UserResponseDTO> getCustomersRegisteredByAgent(int agentId, int page, int size);
	List<CustomerBasicDTO> getAllBasicCustomerInfo();
}
