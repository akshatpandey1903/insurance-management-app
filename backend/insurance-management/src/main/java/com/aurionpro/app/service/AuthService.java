package com.aurionpro.app.service;

import com.aurionpro.app.dto.JwtAuthResponse;
import com.aurionpro.app.dto.LoginDTO;
import com.aurionpro.app.dto.UserRequestDTO;
import com.aurionpro.app.dto.UserResponseDTO;

public interface AuthService {
	JwtAuthResponse login(LoginDTO loginDto);
	UserResponseDTO register(UserRequestDTO requestDto);
}
