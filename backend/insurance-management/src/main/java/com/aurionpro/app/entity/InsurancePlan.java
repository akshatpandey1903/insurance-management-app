package com.aurionpro.app.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	@Column(nullable = false)
	private BigDecimal minCoverageAmount;

	@Column(nullable = false)
	private BigDecimal maxCoverageAmount;

	@Column(nullable = false)
	private int minDurationYears;

	@Column(nullable = false)
	private int maxDurationYears;

	@Column(nullable = false, precision = 5, scale = 2)
	@Min(value = 1, message = "Premium rate must be at least 1")
	private BigDecimal premiumRatePerThousandPerYear;
	
	@Column(nullable = false, length = 255)
	private String description;
	
	@Column(nullable = false)
	@NotNull(message = "Commission rate is required")
    @Min(value = 0, message = "Commission rate must be at least 0%")
	private double commissionRate;
	
	@Column(nullable = false)
	private boolean isActive = true;
	
	@OneToMany(mappedBy = "insurancePlan", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InsurancePlanDocument> requiredDocuments = new ArrayList<>();

}
