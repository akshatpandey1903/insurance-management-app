package com.aurionpro.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateRequestDTO {
	@NotBlank(message = "State name is required")
    private String stateName;
}