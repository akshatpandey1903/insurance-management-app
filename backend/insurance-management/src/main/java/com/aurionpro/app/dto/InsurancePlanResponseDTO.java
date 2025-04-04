package com.aurionpro.app.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InsurancePlanResponseDTO {
	private int insurancePlanId;
    private String planName;
    private String insuranceTypeName;
    private BigDecimal yearlyPremiumAmount;
    private int coverageAmount;
    private int durationYears;
    private String description;
    private double commissionRate;
    private boolean isActive;
}
