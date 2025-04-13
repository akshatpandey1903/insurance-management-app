package com.aurionpro.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.ProfileUpdateRequestDTO;
import com.aurionpro.app.dto.UserResponseDTO;
import com.aurionpro.app.entity.Employee;
import com.aurionpro.app.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserResponseDTO getProfile(int employeeId) {
	    Employee employee = employeeRepository.findByUserIdAndIsActiveTrue(employeeId)
	        .orElseThrow(() -> new RuntimeException("Employee not found"));

	    return new UserResponseDTO(
	        employee.getUserId(),
	        employee.getUsername(),
	        employee.getEmail(),
	        employee.getFirstName(),
	        employee.getLastName(),
	        employee.getRole().getRoleName()
	    );
	}

	@Override
	public UserResponseDTO updateProfile(ProfileUpdateRequestDTO dto) {
	    Employee employee = employeeRepository.findByUserIdAndIsActiveTrue(dto.getEmployeeId())
	        .orElseThrow(() -> new RuntimeException("Employee not found"));

	    if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
	        employee.setUsername(dto.getUsername());
	    }

	    if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
	        employee.setPassword(passwordEncoder.encode(dto.getNewPassword()));
	    }

	    employeeRepository.save(employee);

	    return getProfile(employee.getUserId());
	}

}
