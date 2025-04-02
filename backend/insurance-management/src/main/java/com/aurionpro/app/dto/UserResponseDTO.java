package com.aurionpro.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
	private int id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String roleName;
}
