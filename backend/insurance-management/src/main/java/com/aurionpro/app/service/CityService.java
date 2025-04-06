package com.aurionpro.app.service;

import java.util.List;

import com.aurionpro.app.dto.CityRequestDTO;
import com.aurionpro.app.dto.CityResponseDTO;

public interface CityService {
	CityResponseDTO addCity(CityRequestDTO requestDto);
    List<CityResponseDTO> getCitiesByStateId(int stateId);
    void deleteCity(int id);
}
