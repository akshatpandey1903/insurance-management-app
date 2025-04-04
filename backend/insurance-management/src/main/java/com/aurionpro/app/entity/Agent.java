package com.aurionpro.app.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "agents")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Agent extends User{
	
	@Column(nullable = false)
	private String licenseNumber;
	
	@Column(nullable = false)
	private boolean isApproved = false;
	
	@ManyToOne
	@JoinColumn(name = "approved_by", referencedColumnName = "user_id", nullable = true)
	private Employee approvedBy;
	
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal totalEarnings = BigDecimal.ZERO;
	
	@OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CustomerPolicy> soldPolicies;
}
