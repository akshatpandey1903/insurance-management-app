package com.aurionpro.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InsuranceTypeRequestDTO {
	@NotBlank(message = "Insurance Type name is required")
	private String typeName;
}
