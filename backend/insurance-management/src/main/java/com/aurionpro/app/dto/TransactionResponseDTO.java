package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.aurionpro.app.entity.TransactionType;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {
    private int transactionId;
    private BigDecimal amount;
    private String description;
    private TransactionType transactionType;
    private LocalDateTime transactionTime;
    private String userFullName;
    private String userRole;
    private String paymentReference;
}
