package com.aurionpro.app.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "insurance_plans")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class InsurancePlan {
	
	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int insurancePlanId;
	
	@Column(nullable = false, unique = true)
	private String planName;
	
	@ManyToOne
	@JoinColumn(name = "insurance_type_id", nullable = false)
	private InsuranceType insuranceType;
	
	@Column(nullable = false, precision = 10, scale = 2)
	@NotNull(message = "Premium amount is required")
	@Min(value = 100, message = "Premium must be at least 100")
	private BigDecimal yearlyPremiumAmount;
	
	@Column(nullable = false)
	@NotNull(message = "Coverage amount is required")
    @Min(value = 1000, message = "Coverage amount must be at least 1000")
	private int coverageAmount;
	
	@Column(nullable = false)
	@Min(value = 1, message = "Duration must be at least 1 year")
	private int durationYears;
	
	@Column(nullable = false, length = 255)
	private String description;
	
	@Column(nullable = false)
	@NotNull(message = "Commission rate is required")
    @Min(value = 0, message = "Commission rate must be at least 0%")
	private double commissionRate;
	
	@Column(nullable = false)
	private boolean isActive = true; // Helps in enabling/disabling plans
}
