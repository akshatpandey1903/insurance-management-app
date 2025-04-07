package com.aurionpro.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PlanPurchaseReportDto {
    private int policyId;
    private String customerName;
    private String insuranceType;
    private String planName;
    private LocalDate startDate;
    private BigDecimal calculatedPremium;
    private String status; 
}
