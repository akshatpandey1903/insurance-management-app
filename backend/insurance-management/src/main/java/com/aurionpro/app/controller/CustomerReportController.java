package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.CustomerReportDto;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.service.CustomerReportService;

@RestController
@RequestMapping("/app/reports/customers")
public class CustomerReportController {

    @Autowired
    private CustomerReportService customerReportService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_AGENT')")
    public PageResponse<CustomerReportDto> getCustomerReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return customerReportService.getCustomerReports(pageable);
    }
}
