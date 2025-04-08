package com.aurionpro.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.CustomerQueryRequestDTO;
import com.aurionpro.app.dto.CustomerQueryResponseDTO;
import com.aurionpro.app.service.CustomerQueryService;

@RestController
@RequestMapping("/app/customer/queries")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerQueryController {

    @Autowired
    private CustomerQueryService customerQueryService;

    @PostMapping("/{customerId}")
    public ResponseEntity<CustomerQueryResponseDTO> raiseQuery(
            @RequestBody CustomerQueryRequestDTO dto,
            @PathVariable int customerId) {

        CustomerQueryResponseDTO response = customerQueryService.raiseQuery(dto, customerId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<CustomerQueryResponseDTO>> getQueries(
            @PathVariable int customerId) {

        List<CustomerQueryResponseDTO> queries = customerQueryService.getCustomerQueries(customerId);
        return ResponseEntity.ok(queries);
    }
}

