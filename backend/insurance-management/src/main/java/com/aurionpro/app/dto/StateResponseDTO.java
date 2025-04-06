package com.aurionpro.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateResponseDTO {
	private int stateId;
	private String stateName;
}
