package com.aurionpro.app.service;

import java.util.List;

import com.aurionpro.app.dto.InsurancePlanRequestDTO;
import com.aurionpro.app.dto.InsurancePlanResponseDTO;

public interface InsurancePlanService {
	InsurancePlanResponseDTO createPlan(InsurancePlanRequestDTO requestDto);
	List<InsurancePlanResponseDTO> getAllPlans();
    InsurancePlanResponseDTO getPlanById(int id);
    InsurancePlanResponseDTO updatePlan(int id, InsurancePlanRequestDTO dto);
    void deletePlan(int id);
}