package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.PlanPurchaseReportDto;
import com.aurionpro.app.service.PurchasePlanReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app/admin/reports")
@RequiredArgsConstructor
public class PurchasePlanReportController {
	
	@Autowired
    private final PurchasePlanReportService reportService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/plan-purchases")
    public ResponseEntity<PageResponse<PlanPurchaseReportDto>> getPlanPurchaseReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(reportService.getPlanPurchaseReport(page, size));
    }
}
