package com.aurionpro.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CityResponseDTO {
	private int cityId;
    private String cityName;
    private String stateName;
}