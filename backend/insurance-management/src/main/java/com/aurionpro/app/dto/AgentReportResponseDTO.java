package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentReportResponseDTO {
    private int agentId;
    private String name;
    private String email;
    private String approvedBy;
    private int totalPoliciesRegistered;
    private BigDecimal totalCommissionEarnedYearly;
    private LocalDateTime registeredAt;
}
