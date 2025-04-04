package com.aurionpro.app.service;

import com.aurionpro.app.dto.AdminRegistrationDTO;
import com.aurionpro.app.dto.AgentRegistrationDTO;
import com.aurionpro.app.dto.CustomerRegistrationDTO;
import com.aurionpro.app.dto.EmployeeRegistrationDTO;
import com.aurionpro.app.dto.JwtAuthResponse;
import com.aurionpro.app.dto.LoginDTO;
import com.aurionpro.app.dto.UserResponseDTO;

public interface AuthService {
	JwtAuthResponse login(LoginDTO loginDto);
	UserResponseDTO registerAdmin(AdminRegistrationDTO request);
	UserResponseDTO registerCustomer(CustomerRegistrationDTO request);
	UserResponseDTO registerAgent(AgentRegistrationDTO request);
	UserResponseDTO registerEmployee(EmployeeRegistrationDTO request);
}
