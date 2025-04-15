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

import com.aurionpro.app.dto.CustomerAndPolicyDTO;
import com.aurionpro.app.dto.CustomerBasicDTO;
import com.aurionpro.app.dto.CustomerPolicyResponseDTO;
import com.aurionpro.app.dto.CustomerProfileDTO;
import com.aurionpro.app.dto.CustomerRegistrationDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.UserResponseDTO;
import com.aurionpro.app.service.CustomerPolicyService;
import com.aurionpro.app.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/app/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerPolicyService customerPolicyService;

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
    
    @PostMapping("/agent/{agentId}/register-customer")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<UserResponseDTO> registerCustomerByAgent(
            @PathVariable int agentId,
            @Valid @RequestBody CustomerRegistrationDTO dto
    ) {
        return new ResponseEntity<>(customerService.registerCustomerByAgent(dto, agentId), HttpStatus.CREATED);
    }
    
    @GetMapping("/agent/{agentId}/customers")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<PageResponse<UserResponseDTO>> getCustomersRegisteredByAgent(
            @PathVariable int agentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(customerService.getCustomersRegisteredByAgent(agentId, page, size));
    }
    
    @PostMapping("/register-customer-policy")
    @PreAuthorize("hasRole('AGENT')")
    public CustomerPolicyResponseDTO registerCustomerAndPolicy(
            @RequestBody CustomerAndPolicyDTO dto,
            @RequestParam int agentId) {
        
        UserResponseDTO customer = customerService.registerCustomerByAgent(dto.getCustomerDTO(), agentId);
        return customerPolicyService.registerPolicy(dto.getPolicyDTO(), customer.getId());
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<List<CustomerBasicDTO>> getAllCustomers() {
        List<CustomerBasicDTO> customers = customerService.getAllBasicCustomerInfo();
        return ResponseEntity.ok(customers);
    }

}

