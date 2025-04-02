package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.JwtAuthResponse;
import com.aurionpro.app.dto.LoginDTO;
import com.aurionpro.app.dto.UserRequestDTO;
import com.aurionpro.app.dto.UserResponseDTO;
import com.aurionpro.app.service.AuthService;

@RestController
@RequestMapping("/app")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody @Validated LoginDTO loginDto){
		return ResponseEntity.ok(authService.login(loginDto));
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDTO> register(@RequestBody @Validated UserRequestDTO requestDto){
		return ResponseEntity.ok(authService.register(requestDto));
	}
}
