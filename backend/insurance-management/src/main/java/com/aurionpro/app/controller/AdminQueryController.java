package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.CustomerQueryResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.QueryStatus;
import com.aurionpro.app.service.CustomerQueryService;

@RestController
@RequestMapping("/app/admin/queries")
public class AdminQueryController {

    @Autowired
    private CustomerQueryService customerQueryService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<PageResponse<CustomerQueryResponseDTO>> getAllQueriesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "status", required = false) QueryStatus status) {
    	System.out.println("Query endpoint hit");
        PageResponse<CustomerQueryResponseDTO> response = customerQueryService.getAllQueriesPaginated(page, size, status);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{queryId}/response")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public ResponseEntity<CustomerQueryResponseDTO> respondToQuery(
            @PathVariable int queryId,
            @RequestBody String response) {

        CustomerQueryResponseDTO updated = customerQueryService.respondToQuery(queryId, response);
        return ResponseEntity.ok(updated);
    }
}

