package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentEarningsDTO {
    private String customerName;
    private String insurancePlanName;
    private BigDecimal premiumPaid;
    private double commissionRate;
    private BigDecimal commissionEarned;
    private LocalDate startDate;
}

