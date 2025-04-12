package com.aurionpro.app.dto;

import java.math.BigDecimal;

import com.aurionpro.app.entity.PaymentFrequency;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerPolicyRequestDTO {

    @NotNull(message = "Insurance Plan ID is required")
    private Integer insurancePlanId;

    @NotNull(message = "Payment frequency is required")
    private PaymentFrequency paymentFrequency;

    @NotNull(message = "Selected duration (in years) is required")
    private Integer selectedDurationYears;

    @NotNull(message = "Selected coverage amount is required")
    private BigDecimal selectedCoverageAmount;

    // Optional
    private String licenseNumber;
}
