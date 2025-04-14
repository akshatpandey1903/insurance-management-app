package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.CustomerPolicyRequestDTO;
import com.aurionpro.app.dto.CustomerPolicyResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.service.CustomerPolicyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/app/policy")
public class CustomerPolicyController {
	
	@Autowired
	private CustomerPolicyService customerPolicyService;
	
	@PostMapping("/register/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('AGENT')")
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
	
	@PreAuthorize("hasRole('CUSTOMER')")
	@PutMapping("/cancel/{customerId}/{policyId}")
	public ResponseEntity<CustomerPolicyResponseDTO> cancelPolicy(
	        @PathVariable int policyId,
	        @PathVariable int customerId
	) {
	    CustomerPolicyResponseDTO responseDTO = customerPolicyService.cancelPolicy(customerId, policyId);
	    return ResponseEntity.ok(responseDTO);
	}
	
	@GetMapping("/unapproved")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public ResponseEntity<PageResponse<CustomerPolicyResponseDTO>> getUnapprovedPolicies(
	        @RequestParam(defaultValue = "0") int page, 
	        @RequestParam(defaultValue = "10") int size) {
	    PageResponse<CustomerPolicyResponseDTO> response = customerPolicyService.getUnapprovedPolicies(page, size);
	    return ResponseEntity.ok(response);
	}
	
	@PutMapping("/reject/{policyId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public ResponseEntity<String> rejectPolicy(
	        @PathVariable int policyId,
	        @RequestParam("employeeId") int employeeId,
	        @RequestParam("reason") String reason) {

	    customerPolicyService.rejectCustomerPolicy(policyId, employeeId, reason);
	    return ResponseEntity.ok("Policy rejected successfully.");
	}

}
