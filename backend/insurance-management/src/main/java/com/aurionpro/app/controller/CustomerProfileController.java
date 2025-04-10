package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.CustomerProfileDTO;
import com.aurionpro.app.repository.UserRepository;
import com.aurionpro.app.service.CustomerProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app/customer")
@RequiredArgsConstructor
public class CustomerProfileController {

	@Autowired
    private final CustomerProfileService customerProfileService;

    @GetMapping("/profile")
    public ResponseEntity<CustomerProfileDTO> getProfile(Authentication auth) {
        String username = auth.getName();
        return ResponseEntity.ok(customerProfileService.getProfile(username));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody CustomerProfileDTO dto, Authentication auth) {
        try {
            String username = auth.getName();
            customerProfileService.updateProfile(username, dto);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
