package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.AgentCommissionReportDto;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.service.AgentReportService;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/app/admin/reports")
public class AgentReportController {

    @Autowired
    private AgentReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/agent-commissions")
    public PageResponse<AgentCommissionReportDto> getAgentCommissionReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return reportService.getAgentCommissionReport(pageable);
    }
}
