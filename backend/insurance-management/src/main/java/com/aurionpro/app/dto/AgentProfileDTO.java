package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AgentProfileDTO {
	private int id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private boolean active;
	private String licenseNumber;
	private boolean approved;
	private String approvedBy;
	private BigDecimal totalEarnings;
	private LocalDateTime createdAt;
	private String currentPassword;
    private String newPassword;
	
	public AgentProfileDTO(int id, String username, String email, String firstName, String lastName, boolean active,
			String licenseNumber, boolean approved, String approvedBy, BigDecimal totalEarnings,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
		this.licenseNumber = licenseNumber;
		this.approved = approved;
		this.approvedBy = approvedBy;
		this.totalEarnings = totalEarnings;
		this.createdAt = createdAt;
	}

	public AgentProfileDTO(int id, String username, String email, String firstName, String lastName, boolean active,
			String licenseNumber, boolean approved, String approvedBy, BigDecimal totalEarnings,
			LocalDateTime createdAt, String currentPassword, String newPassword) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
		this.licenseNumber = licenseNumber;
		this.approved = approved;
		this.approvedBy = approvedBy;
		this.totalEarnings = totalEarnings;
		this.createdAt = createdAt;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}
	
	
	
}
