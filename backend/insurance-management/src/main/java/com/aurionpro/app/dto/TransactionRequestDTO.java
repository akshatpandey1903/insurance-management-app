package com.aurionpro.app.dto;

import java.math.BigDecimal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    private int customerPolicyId;
    private BigDecimal amount;
}
