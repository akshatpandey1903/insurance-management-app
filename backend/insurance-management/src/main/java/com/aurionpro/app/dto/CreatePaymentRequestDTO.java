package com.aurionpro.app.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreatePaymentRequestDTO {
    private BigDecimal amount;
    private String currency = "INR";  // default to INR
    private String receipt;
}
