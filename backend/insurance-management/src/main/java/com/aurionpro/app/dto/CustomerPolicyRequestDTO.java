package com.aurionpro.app.dto;

import java.time.LocalDate;

import com.aurionpro.app.entity.PaymentFrequency;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerPolicyRequestDTO {

    @NotNull(message = "Insurance Plan ID is required")
    private Integer insurancePlanId;

    @NotNull(message = "Payment frequency is required")
    private PaymentFrequency paymentFrequency;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    // Optional 
    private Integer agentId;
}
