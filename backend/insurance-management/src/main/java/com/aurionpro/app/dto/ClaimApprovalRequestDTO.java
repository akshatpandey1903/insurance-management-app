package com.aurionpro.app.dto;

import com.aurionpro.app.entity.WithdrawalStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ClaimApprovalRequestDTO {
    private int claimId;
    private int employeeId;
    private WithdrawalStatus status;
    private String remarks;
}
