package com.aurionpro.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AgentResponseDTO {
	private int userId;
    private String fullName;
    private String username;
    private String email;
    private String licenseNumber;
    private boolean isApproved;
}
