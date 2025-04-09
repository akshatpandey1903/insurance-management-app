package com.aurionpro.app.controller;

import java.util.List;

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

import com.aurionpro.app.dto.ClaimApprovalRequestDTO;
import com.aurionpro.app.dto.ClaimFilterRequestDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.PolicyClaimRequestDTO;
import com.aurionpro.app.dto.PolicyClaimResponseDTO;
import com.aurionpro.app.service.PolicyClaimService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app/customer/claims")
@RequiredArgsConstructor
public class PolicyClaimController {
	
	@Autowired
    private final PolicyClaimService policyClaimService;

    @PostMapping("/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PolicyClaimResponseDTO> raiseClaim(@RequestBody @Valid PolicyClaimRequestDTO request,
                                                             @PathVariable int customerId) {
        PolicyClaimResponseDTO response = policyClaimService.raiseClaim(request, customerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PolicyClaimResponseDTO>> getCustomerClaims(@PathVariable int customerId) {
        return ResponseEntity.ok(policyClaimService.getClaimsForCustomer(customerId));
    }
    
    @PutMapping("/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<PolicyClaimResponseDTO> approveClaim(@RequestBody ClaimApprovalRequestDTO requestDTO) {
        return ResponseEntity.ok(policyClaimService.approveClaim(requestDTO));
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @PostMapping("/all")
    public ResponseEntity<PageResponse<PolicyClaimResponseDTO>> getAllClaims(
            @RequestBody ClaimFilterRequestDTO filterDTO,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {

        return ResponseEntity.ok(policyClaimService.getAllClaims(filterDTO, pageNumber, pageSize));
    }

}

