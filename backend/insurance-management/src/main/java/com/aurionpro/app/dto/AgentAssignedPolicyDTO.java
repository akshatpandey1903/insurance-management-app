package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class AgentAssignedPolicyDTO {
    private String customerName;
    private String insurancePlanName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal premiumAmount;
    private BigDecimal commissionAmount;
}
