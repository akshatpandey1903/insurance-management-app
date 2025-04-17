package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.AdminProfileDto;
import com.aurionpro.app.service.CustomerProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {
	
	@Autowired
    private final CustomerProfileService customerProfileService;
	
	@GetMapping
	public ResponseEntity<AdminProfileDto> getProfile(Authentication auth)
	{
		String username = auth.getName();
//		System.out.println("Inside Get admin controller" + username);
		return ResponseEntity.ok(customerProfileService.getAdminProfile(username));
	}
	
	@PutMapping
    public ResponseEntity<String> updateProfile(@RequestBody AdminProfileDto dto, Authentication auth) {
        try {
            String username = auth.getName();
            customerProfileService.updateAdminProfile(username, dto);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
