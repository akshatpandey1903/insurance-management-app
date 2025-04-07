package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aurionpro.app.entity.WithdrawalStatus;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalResponseDTO {
    private int withdrawalId;
    private BigDecimal amount;
    private String userRole;
    private int requestedById;
    private String requestedByName;
    private WithdrawalStatus status;
    private LocalDateTime requestedAt;
    private Integer approvedById;
    private String approvedByName;
    private LocalDateTime approvedAt;
    private String remarks;
}
