package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.aurionpro.app.entity.PaymentFrequency;

import lombok.Data;

@Data
public class CustomerPolicyResponseDTO {

    private int id;

    private String customerName;

    private String insurancePlanName;

    private PaymentFrequency paymentFrequency;

    private BigDecimal calculatedPremium;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isActive;

    private String approvedBy;

    private String agentName;
}
