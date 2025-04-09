package com.aurionpro.app.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer extends User{
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false)
    private String phoneNumber;
	
	@OneToMany(mappedBy = "customer")
	private List<CustomerPolicy> customerPolicies;
	
	@ManyToOne
	@JoinColumn(name = "registered_by")
	private Agent registeredBy;

}
