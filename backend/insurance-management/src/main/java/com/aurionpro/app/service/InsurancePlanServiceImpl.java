package com.aurionpro.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.InsurancePlanRequestDTO;
import com.aurionpro.app.dto.InsurancePlanResponseDTO;
import com.aurionpro.app.entity.DocumentType;
import com.aurionpro.app.entity.InsurancePlan;
import com.aurionpro.app.entity.InsurancePlanDocument;
import com.aurionpro.app.entity.InsuranceType;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.InsurancePlanDocumentRepository;
import com.aurionpro.app.repository.InsurancePlanRepository;
import com.aurionpro.app.repository.InsuranceTypeRepository;

@Service
public class InsurancePlanServiceImpl implements InsurancePlanService{
	
	@Autowired
	private InsurancePlanDocumentRepository insurancePlanDocumentRepository;
	
	@Autowired
	private InsurancePlanRepository insurancePlanRepository;
	
	@Autowired
    private InsuranceTypeRepository typeRepository;
	
	private InsurancePlanResponseDTO mapToResponse(InsurancePlan plan) {
	    List<DocumentType> requiredDocs = insurancePlanDocumentRepository.findByInsurancePlan(plan)
	            .stream()
	            .map(InsurancePlanDocument::getDocumentType)
	            .collect(Collectors.toList());

	    return new InsurancePlanResponseDTO(
	    	    plan.getInsurancePlanId(),
	    	    plan.getPlanName(),
	    	    plan.getInsuranceType().getTypeName(),
	    	    plan.getMinCoverageAmount(),
	    	    plan.getMaxCoverageAmount(),
	    	    plan.getMinDurationYears(),
	    	    plan.getMaxDurationYears(),
	    	    plan.getPremiumRatePerThousandPerYear(),
	    	    plan.getDescription(),
	    	    plan.getCommissionRate(),
	    	    plan.isActive(),
	    	    requiredDocs
	    	);

	}

	@Override
	public InsurancePlanResponseDTO createPlan(InsurancePlanRequestDTO requestDto) {
	    InsuranceType insuranceType = typeRepository.findById(requestDto.getInsuranceTypeId())
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Insurance Type not found"));

	    InsurancePlan plan = new InsurancePlan();
	    plan.setPlanName(requestDto.getPlanName());
	    plan.setInsuranceType(insuranceType);
	    plan.setDescription(requestDto.getDescription());
	    plan.setCommissionRate(requestDto.getCommissionRate());
	    plan.setActive(requestDto.isActive());
	    plan.setMinCoverageAmount(requestDto.getMinCoverageAmount());
	    plan.setMaxCoverageAmount(requestDto.getMaxCoverageAmount());
	    plan.setMinDurationYears(requestDto.getMinDurationYears());
	    plan.setMaxDurationYears(requestDto.getMaxDurationYears());
	    plan.setPremiumRatePerThousandPerYear(requestDto.getPremiumRatePerThousandPerYear());


	    InsurancePlan savedPlan = insurancePlanRepository.save(plan);
	    
	    List<DocumentType> documentTypes = requestDto.getRequiredDocuments().stream()
	            .map(DocumentType::valueOf)
	            .collect(Collectors.toList());

	    saveRequiredDocuments(savedPlan, documentTypes);

	    return mapToResponse(savedPlan);
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
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Insurance Type not found"));

	    InsuranceType insuranceType = typeRepository.findById(dto.getInsuranceTypeId())
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Insurance Type not found"));

	    plan.setPlanName(dto.getPlanName());
	    plan.setInsuranceType(insuranceType);
	    plan.setDescription(dto.getDescription());
	    plan.setCommissionRate(dto.getCommissionRate());
	    plan.setActive(dto.isActive());
	    plan.setMinCoverageAmount(dto.getMinCoverageAmount());
	    plan.setMaxCoverageAmount(dto.getMaxCoverageAmount());
	    plan.setMinDurationYears(dto.getMinDurationYears());
	    plan.setMaxDurationYears(dto.getMaxDurationYears());
	    plan.setPremiumRatePerThousandPerYear(dto.getPremiumRatePerThousandPerYear());


	    InsurancePlan updated = insurancePlanRepository.save(plan);
	    
	    List<DocumentType> documentTypes = dto.getRequiredDocuments().stream()
	            .map(DocumentType::valueOf)
	            .collect(Collectors.toList());

	    saveRequiredDocuments(updated, documentTypes);

	    return mapToResponse(updated);
	}


	@Override
	public void deletePlan(int id) {
		InsurancePlan plan = insurancePlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Insurance Type not found"));
		plan.setActive(false);
		insurancePlanRepository.save(plan);
	}
	
	private void saveRequiredDocuments(InsurancePlan plan, List<DocumentType> documentTypes) {
	    // First delete existing mappings (useful during update)
	    insurancePlanDocumentRepository.deleteByInsurancePlan(plan);

	    // Then create new entries
	    List<InsurancePlanDocument> documents = documentTypes.stream()
	            .map(type -> new InsurancePlanDocument(plan, type))
	            .collect(Collectors.toList());

	    insurancePlanDocumentRepository.saveAll(documents);
	}


}
