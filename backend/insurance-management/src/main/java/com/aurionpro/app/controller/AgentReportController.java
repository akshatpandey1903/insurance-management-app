package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.AgentCommissionReportDto;
import com.aurionpro.app.dto.AgentReportResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.service.AgentReportService;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/app/admin/reports")
public class AgentReportController {

    @Autowired
    private AgentReportService reportService;
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @GetMapping("/agents")
    public ResponseEntity<PageResponse<AgentReportResponseDTO>> getAgentReport(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {

        return ResponseEntity.ok(reportService.getAgentReport(pageNumber, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    @GetMapping("/agent-commissions")
    public PageResponse<AgentCommissionReportDto> getAgentCommissionReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return reportService.getAgentCommissionReport(pageable);
    }
}
