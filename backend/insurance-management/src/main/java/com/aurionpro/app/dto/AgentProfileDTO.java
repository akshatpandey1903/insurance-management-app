package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
}
