package com.aurionpro.app.service;

import java.util.List;

import com.aurionpro.app.dto.InsuranceTypeRequestDTO;
import com.aurionpro.app.dto.InsuranceTypeResponseDTO;

public interface InsuranceTypeService {
	InsuranceTypeResponseDTO createInsuranceType(InsuranceTypeRequestDTO requestDto);
	List<InsuranceTypeResponseDTO> getAllInsuranceTypes();
	InsuranceTypeResponseDTO getInsuranceTypeById(int id);
	InsuranceTypeResponseDTO updateInsuranceType(int id, InsuranceTypeRequestDTO requestDto);
	void deleteInsuranceType(int id);
}
