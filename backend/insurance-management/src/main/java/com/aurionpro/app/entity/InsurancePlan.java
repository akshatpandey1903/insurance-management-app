package com.aurionpro.app.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "insurance_plans")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class InsurancePlan {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int insurancePlanId;
	
	@Column(nullable = false, unique = true)
	private String planName;
	
	@ManyToOne
	@JoinColumn(name = "insurance_type", nullable = false)
	private InsuranceType insuranceType;
	
	@Column(nullable = false)
	private BigDecimal yearlyPremiumAmount;
	
	@Column(nullable = false)
	private int coverageAmount;
	
	@Column(nullable = false)
	private int durationYears;
	
	@Column(nullable = false)
	private String description;
}
