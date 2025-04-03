package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.ForgotPasswordRequestDTO;
import com.aurionpro.app.dto.ResetPasswordRequestDTO;
import com.aurionpro.app.service.PasswordResetTokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/app/password")
public class PasswordResetController 
{
	@Autowired
    private PasswordResetTokenService passwordResetTokenService;
	
	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequestDTO requestDTO)
	{
		passwordResetTokenService.createPasswordResetToken(requestDTO.getEmail());
		return ResponseEntity.ok("Password reset link sent to email.");
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO requestDTO) 
	{
	    passwordResetTokenService.resetPassword(requestDTO.getToken(), requestDTO.getNewPassword());
	    return ResponseEntity.ok("Password successfully reset.");
	}
	
	
}
