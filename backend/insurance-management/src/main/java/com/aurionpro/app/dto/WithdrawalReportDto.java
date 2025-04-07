package com.aurionpro.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aurionpro.app.entity.WithdrawalStatus;

@Data
@AllArgsConstructor
public class WithdrawalReportDto {
    private int withdrawalId;
    private String requestedBy;
    private String userRole;
    private BigDecimal amount;
    private WithdrawalStatus status;
    private LocalDateTime requestedAt;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private String remarks;
}
