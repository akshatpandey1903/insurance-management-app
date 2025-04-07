package com.aurionpro.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.aurionpro.app.entity.PaymentFrequency;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PlanPurchaseReportDto {
    private int policyId;
    private String customerName;
    private String phoneNumber;
    private String insuranceType;
    private String planName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal calculatedPremium;
    private PaymentFrequency paymentFrequency;
    private String agentName;
    private String status;
}
