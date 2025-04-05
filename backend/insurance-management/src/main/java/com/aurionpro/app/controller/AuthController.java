package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; // chng
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.AdminRegistrationDTO;
import com.aurionpro.app.dto.AgentRegistrationDTO;
import com.aurionpro.app.dto.CustomerRegistrationDTO;
import com.aurionpro.app.dto.EmployeeRegistrationDTO;
import com.aurionpro.app.dto.JwtAuthResponse;
import com.aurionpro.app.dto.LoginDTO;
import com.aurionpro.app.dto.UserResponseDTO;
import com.aurionpro.app.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/app")
@CrossOrigin(origins = "http://localhost:4200") // line chng
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid LoginDTO loginDto){
		return ResponseEntity.ok(authService.login(loginDto));
	}
	
	@PostMapping("/register/admin")
	public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid AdminRegistrationDTO requestDto){
		return ResponseEntity.ok(authService.registerAdmin(requestDto));
	}
	
	@PostMapping("/register/customer")
	public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid CustomerRegistrationDTO requestDto){
		return ResponseEntity.ok(authService.registerCustomer(requestDto));
	}
	
	@PostMapping("/register/agent")
	public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid AgentRegistrationDTO requestDto){
		return ResponseEntity.ok(authService.registerAgent(requestDto));
	}
	
	@PostMapping("/register/employee")
	public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid EmployeeRegistrationDTO requestDto){
		return ResponseEntity.ok(authService.registerEmployee(requestDto));
	}
}
