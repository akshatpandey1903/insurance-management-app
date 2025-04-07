package com.aurionpro.app.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PayoutRequestDTO {
    private String accountNumber;
    private BigDecimal amount;
    private String name;
    private String ifsc;
    private String purpose;
    private String remarks;
}

