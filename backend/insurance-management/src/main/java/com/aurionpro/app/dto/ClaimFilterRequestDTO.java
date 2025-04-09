package com.aurionpro.app.dto;

import java.time.LocalDate;

import com.aurionpro.app.entity.WithdrawalStatus;

import lombok.Data;

@Data
public class ClaimFilterRequestDTO {
    private WithdrawalStatus status;
    private String customerName;
    private LocalDate fromDate;
    private LocalDate toDate;
}
