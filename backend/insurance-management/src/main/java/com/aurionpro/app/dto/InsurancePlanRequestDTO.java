package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InsurancePlanRequestDTO {
	
	@NotBlank(message = "Plan name is required")
	private String planName;
	
	@NotNull(message = "Insurance Type Id can not be null")
	private Integer insuranceTypeId;
	
	@NotNull(message = "Premium amount is required")
	@DecimalMin(value = "100.00", message = "Premium must atleast be 100")
	private BigDecimal yearlyPremiumAmount;
	
	@NotNull(message = "Coverage amount is required")
    @Min(value = 1000, message = "Coverage amount must be at least 1000")
	private Integer coverageAmount;
	
	@Min(value = 1, message = "Duration must be at least 1 year")
	private int durationYears;
	
	@NotBlank(message = "Description is required")
    private String description;
	
	@NotNull(message = "Commission rate is required")
	@Min(value = 0, message = "Commission rate must be at least 0")
    private Double commissionRate;
	
	private boolean isActive = true;
	
	private List<String> requiredDocuments;
}
