package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.CustomerPolicyRequestDTO;
import com.aurionpro.app.dto.CustomerPolicyResponseDTO;
import com.aurionpro.app.service.CustomerPolicyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/app/policy")
public class CustomerPolicyController {
	
	@Autowired
	private CustomerPolicyService customerPolicyService;
	
	@PostMapping("/register/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerPolicyResponseDTO> registerPolicy(
            @RequestBody @Valid CustomerPolicyRequestDTO requestDTO,
            @PathVariable int customerId) {

        CustomerPolicyResponseDTO responseDTO = customerPolicyService.registerPolicy(requestDTO, customerId);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
	
	@PutMapping("/approve/{policyId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public ResponseEntity<CustomerPolicyResponseDTO> approvePolicy(
	        @PathVariable int policyId,
	        @RequestParam("employeeId") int employeeId) {
	    
	    CustomerPolicyResponseDTO responseDTO = customerPolicyService.approveCustomerPolicy(policyId, employeeId);
	    return ResponseEntity.ok(responseDTO);
	}

}
