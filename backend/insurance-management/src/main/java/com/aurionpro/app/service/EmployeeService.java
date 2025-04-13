package com.aurionpro.app.service;

import com.aurionpro.app.dto.ProfileUpdateRequestDTO;
import com.aurionpro.app.dto.UserResponseDTO;

public interface EmployeeService {
	UserResponseDTO getProfile(int employeeId);
	UserResponseDTO updateProfile(ProfileUpdateRequestDTO dto);
}
