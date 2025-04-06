package com.aurionpro.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityRequestDTO {
	@NotBlank(message = "City name is required")
	private String cityName;
	
	@NotNull(message = "State ID is required")
    private int stateId;
}
