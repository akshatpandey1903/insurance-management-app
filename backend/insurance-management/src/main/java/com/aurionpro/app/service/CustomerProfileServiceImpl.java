package com.aurionpro.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.AdminProfileDto;
import com.aurionpro.app.dto.CustomerProfileDTO;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.User;
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
	    dto.setEmail(customer.getEmail());

	    return dto;
	}
	
	@Override
	public AdminProfileDto getAdminProfile(String username) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		AdminProfileDto dto = new AdminProfileDto();
		
		dto.setUsername(user.getUsername());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		dto.setRole(user.getRole().getRoleName());
		
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

	@Override
	public void updateAdminProfile(String username, AdminProfileDto dto) {
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		if (dto.getCurrentPassword() == null || !passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
	        throw new RuntimeException("Incorrect current password");
	    }
		
		if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
	    if (dto.getLastName() != null) user.setLastName(dto.getLastName());
	    if (dto.getEmail() != null) user.setEmail(dto.getEmail());
	    if (dto.getUsername() != null) user.setUsername(dto.getUsername());

	    if (dto.getNewPassword() != null && !dto.getNewPassword().trim().isEmpty()) {
	        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
	    }
		
		userRepo.save(user);
	}

}
