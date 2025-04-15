package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.aurionpro.app.entity.Employee;
import com.aurionpro.app.entity.PaymentFrequency;

import lombok.Data;

@Data
public class PurchasedPolicyDto {
	private int policyId;
    private String planName;
    private PaymentFrequency paymentFrequency;
    private BigDecimal calculatedPremium;
    private BigDecimal selectedCoverageAmount;
    private Integer selectedDurationYears;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextDueDate;
    private String approvedBy;
}
