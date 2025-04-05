package com.aurionpro.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.InsuranceTypeRequestDTO;
import com.aurionpro.app.dto.InsuranceTypeResponseDTO;
import com.aurionpro.app.entity.InsuranceType;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.InsuranceTypeRepository;

@Service
public class InsuranceTypeServiceImpl implements InsuranceTypeService{
	
	@Autowired
	InsuranceTypeRepository insuranceTypeRepository;

	@Override
	public InsuranceTypeResponseDTO createInsuranceType(InsuranceTypeRequestDTO requestDto) {
		InsuranceType insuranceType = new InsuranceType();
		insuranceType.setTypeName(requestDto.getTypeName());
		InsuranceType saved = insuranceTypeRepository.save(insuranceType);
		return new InsuranceTypeResponseDTO(saved.getInsuranceTypeId(), saved.getTypeName());
	}

	@Override
	public List<InsuranceTypeResponseDTO> getAllInsuranceTypes() {
		return insuranceTypeRepository.findAll()
				.stream()
				.map(type -> new InsuranceTypeResponseDTO(type.getInsuranceTypeId(), type.getTypeName()))
				.collect(Collectors.toList());
	}

	@Override
	public InsuranceTypeResponseDTO getInsuranceTypeById(int id) {
		InsuranceType type = insuranceTypeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Insurance Type not found with id " + id));
		return new InsuranceTypeResponseDTO(type.getInsuranceTypeId(), type.getTypeName());
	}

	@Override
	public InsuranceTypeResponseDTO updateInsuranceType(int id, InsuranceTypeRequestDTO requestDto) {
		InsuranceType type = insuranceTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Insurance Type not found with id " + id));
		type.setTypeName(requestDto.getTypeName());
		InsuranceType updated = insuranceTypeRepository.save(type);
		return new InsuranceTypeResponseDTO(updated.getInsuranceTypeId(), updated.getTypeName());
	}

	@Override
	public void deleteInsuranceType(int id) {
		InsuranceType type = insuranceTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Insurance Type not found with id " + id));
        insuranceTypeRepository.delete(type);	
	}

}
