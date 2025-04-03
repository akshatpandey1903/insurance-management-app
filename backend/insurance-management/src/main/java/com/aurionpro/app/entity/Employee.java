package com.aurionpro.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Employee extends User{
	
	@Column(nullable = false)
	private String department;
	
	@Column(nullable = false)
	private String designation;
}
