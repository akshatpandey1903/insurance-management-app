package com.aurionpro.app.dto;

import com.aurionpro.app.entity.PaymentFrequency;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerPolicyRequestDTO {

    @NotNull(message = "Insurance Plan ID is required")
    private Integer insurancePlanId;

    @NotNull(message = "Payment frequency is required")
    private PaymentFrequency paymentFrequency;

    // Optional 
    private Integer agentId;
}
