package com.aurionpro.app.service;

import com.aurionpro.app.dto.CustomerProfileDTO;

public interface CustomerService {
	CustomerProfileDTO viewProfile(int customerId);
	CustomerProfileDTO updateProfile(int customerId, CustomerProfileDTO dto);
}
