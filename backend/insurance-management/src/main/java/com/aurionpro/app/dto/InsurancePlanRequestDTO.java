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
	
	@NotNull(message = "Minimum coverage is required")
	@Min(value = 1000, message = "Minimum coverage must be at least 1000")
	private BigDecimal minCoverageAmount;

	@NotNull(message = "Maximum coverage is required")
	@Min(value = 1000, message = "Maximum coverage must be at least 1000")
	private BigDecimal maxCoverageAmount;

	@NotNull(message = "Minimum duration is required")
	@Min(value = 1, message = "Minimum duration must be at least 1 year")
	private Integer minDurationYears;

	@NotNull(message = "Maximum duration is required")
	@Min(value = 1, message = "Maximum duration must be at least 1 year")
	private Integer maxDurationYears;

	@NotNull(message = "Premium rate is required")
	@DecimalMin(value = "0.01", message = "Premium rate must be at least 0.01")
	private BigDecimal premiumRatePerThousandPerYear;
	
	@NotBlank(message = "Description is required")
    private String description;
	
	@NotNull(message = "Commission rate is required")
	@Min(value = 0, message = "Commission rate must be at least 0")
    private Double commissionRate;
	
	private boolean isActive = true;
	
	private List<String> requiredDocuments;
}
