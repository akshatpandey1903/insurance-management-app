package com.aurionpro.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.CustomerProfileDTO;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.repository.UserRepository;

@Service
public class CustomerProfileServiceImpl implements CustomerProfileService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public CustomerProfileDTO getProfile(String username) {
	    Customer customer = (Customer) userRepo.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    CustomerProfileDTO dto = new CustomerProfileDTO();
	    dto.setFirstName(customer.getFirstName());
	    dto.setLastName(customer.getLastName());
	    dto.setAddress(customer.getAddress());
	    dto.setPhone(customer.getPhoneNumber());

	    return dto;
	}

	@Override
	public void updateProfile(String username, CustomerProfileDTO dto) {
	    Customer customer = (Customer) userRepo.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    if (dto.getCurrentPassword() == null || !passwordEncoder.matches(dto.getCurrentPassword(), customer.getPassword())) {
	        throw new RuntimeException("Incorrect current password");
	    }

	    if (dto.getFirstName() != null) customer.setFirstName(dto.getFirstName());
	    if (dto.getLastName() != null) customer.setLastName(dto.getLastName());
	    if (dto.getPhone() != null) customer.setPhoneNumber(dto.getPhone());
	    if (dto.getAddress() != null) customer.setAddress(dto.getAddress());

	    if (dto.getNewPassword() != null && !dto.getNewPassword().trim().isEmpty()) {
	        customer.setPassword(passwordEncoder.encode(dto.getNewPassword()));
	    }


	    userRepo.save(customer);
	}

}
