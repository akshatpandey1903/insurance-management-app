package com.aurionpro.app.dto;

import com.aurionpro.app.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AdminProfileDto {

	private String username;
    private String currentPassword;
    private String newPassword;
	private String email;
	private String firstName;
	private String lastName;
	private String role;
}
