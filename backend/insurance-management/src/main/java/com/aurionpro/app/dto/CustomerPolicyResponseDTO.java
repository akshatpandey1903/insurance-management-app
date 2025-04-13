package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.aurionpro.app.entity.PaymentFrequency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomerPolicyResponseDTO {

    private int id;

    private String customerName;

    private String insurancePlanName;

    private PaymentFrequency paymentFrequency;

    private BigDecimal calculatedPremium;

    private BigDecimal selectedCoverageAmount;

    private Integer selectedDurationYears;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate nextDueDate;

    private boolean isActive;

    private String approvedBy;

    private String agentName;
}
