package com.aurionpro.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.InsurancePlanRequestDTO;
import com.aurionpro.app.dto.InsurancePlanResponseDTO;
import com.aurionpro.app.entity.InsurancePlan;
import com.aurionpro.app.entity.InsuranceType;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.InsurancePlanRepository;
import com.aurionpro.app.repository.InsuranceTypeRepository;

@Service
public class InsurancePlanServiceImpl implements InsurancePlanService{
	
	@Autowired
	private InsurancePlanRepository insurancePlanRepository;
	
	@Autowired
    private InsuranceTypeRepository typeRepository;
	
	private InsurancePlanResponseDTO mapToResponse(InsurancePlan plan) {
        return new InsurancePlanResponseDTO(
                plan.getInsurancePlanId(),
                plan.getPlanName(),
                plan.getInsuranceType().getTypeName(),
                plan.getYearlyPremiumAmount(),
                plan.getCoverageAmount(),
                plan.getDurationYears(),
                plan.getDescription(),
                plan.getCommissionRate(),
                plan.isActive()
        );
    }

	@Override
	public InsurancePlanResponseDTO createPlan(InsurancePlanRequestDTO requestDto) {
		InsuranceType insuranceType = typeRepository.findById(requestDto.getInsuranceTypeId())
				.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Insurance Type not found"));
		
		InsurancePlan plan = new InsurancePlan();
		plan.setPlanName(requestDto.getPlanName());
		plan.setInsuranceType(insuranceType);
		plan.setYearlyPremiumAmount(requestDto.getYearlyPremiumAmount());
		plan.setCoverageAmount(requestDto.getCoverageAmount());
		plan.setDescription(requestDto.getDescription());
		plan.setDurationYears(requestDto.getDurationYears());
		plan.setCommissionRate(requestDto.getCommissionRate());
		plan.setActive(requestDto.isActive());
		
		InsurancePlan saved = insurancePlanRepository.save(plan);

        return mapToResponse(saved);
	}

	@Override
	public List<InsurancePlanResponseDTO> getAllPlans() {
		// TODO Auto-generated method stub
		return insurancePlanRepository.findAll()
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public InsurancePlanResponseDTO getPlanById(int id) {
		InsurancePlan insurancePlan = insurancePlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Insurance Type not found"));
		return mapToResponse(insurancePlan);
	}

	@Override
	public InsurancePlanResponseDTO updatePlan(int id, InsurancePlanRequestDTO dto) {
		InsurancePlan plan = insurancePlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Insurance Type not found"));
		
		InsuranceType insuranceType = typeRepository.findById(dto.getInsuranceTypeId())
				.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Insurance Type not found"));
		
		plan.setPlanName(dto.getPlanName());
        plan.setInsuranceType(insuranceType);
        plan.setYearlyPremiumAmount(dto.getYearlyPremiumAmount());
        plan.setCoverageAmount(dto.getCoverageAmount());
        plan.setDurationYears(dto.getDurationYears());
        plan.setDescription(dto.getDescription());
        plan.setCommissionRate(dto.getCommissionRate());
        plan.setActive(dto.isActive());

        InsurancePlan updated = insurancePlanRepository.save(plan);
        return mapToResponse(updated);
	}

	@Override
	public void deletePlan(int id) {
		InsurancePlan plan = insurancePlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Insurance Type not found"));
		insurancePlanRepository.delete(plan);
	}

}
