package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.ProfileUpdateRequestDTO;
import com.aurionpro.app.dto.UserResponseDTO;
import com.aurionpro.app.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app/employee")
@RequiredArgsConstructor
public class EmployeeController {
	
	@Autowired
    private final EmployeeService employeeService;

    @GetMapping("/profile/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponseDTO> getProfile(@PathVariable int employeeId) {
        return ResponseEntity.ok(employeeService.getProfile(employeeId));
    }

    @PutMapping("/profile/update")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponseDTO> updateProfile(@RequestBody ProfileUpdateRequestDTO dto) {
        return ResponseEntity.ok(employeeService.updateProfile(dto));
    }
}
