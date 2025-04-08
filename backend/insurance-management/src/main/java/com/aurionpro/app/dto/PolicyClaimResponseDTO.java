package com.aurionpro.app.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyClaimResponseDTO {
    private int id;
    private int policyId;
    private String planName;
    private String reason;
    private String status;
    private String remarks;
    private String verifiedBy;
    private LocalDateTime requestedAt;
}
