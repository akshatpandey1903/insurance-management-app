package com.aurionpro.app.dto;

import lombok.Data;

@Data
public class CreatePaymentResponseDTO {
    private String orderId;
    private String currency;
    private String receipt;
    private int amount;
}