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

import jakarta.validation.Valid;

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
<<<<<<< HEAD
	public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRequestDTO requestDto){
=======
	public ResponseEntity<UserResponseDTO> register(@RequestBody @Validated UserRequestDTO requestDto){
>>>>>>> 768ccaa9484f81013ea3c3596894d8e9df83e6e0
		return ResponseEntity.ok(authService.register(requestDto));
	}
}
