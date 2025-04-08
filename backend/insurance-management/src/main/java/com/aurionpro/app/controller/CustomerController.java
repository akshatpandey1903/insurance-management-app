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

import com.aurionpro.app.dto.CustomerProfileDTO;
import com.aurionpro.app.service.CustomerService;

@RestController
@RequestMapping("/app/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/profile/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerProfileDTO> viewProfile(@PathVariable int customerId) {
        return ResponseEntity.ok(customerService.viewProfile(customerId));
    }

    @PutMapping("/profile/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerProfileDTO> updateProfile(@PathVariable int customerId,
                                                             @RequestBody CustomerProfileDTO dto) {
        return ResponseEntity.ok(customerService.updateProfile(customerId, dto));
    }
}

