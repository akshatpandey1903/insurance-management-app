package com.aurionpro.app.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "insurance_types")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class InsuranceType {
	
	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int insuranceTypeId;
	
	@Column(nullable = false, unique = true)
	private String typeName;
	
	@OneToMany(mappedBy = "insurance_type", cascade = CascadeType.ALL)
	private List<InsurancePlan> insurancePlans;
}
