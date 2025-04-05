package com.aurionpro.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.InsuranceTypeRequestDTO;
import com.aurionpro.app.dto.InsuranceTypeResponseDTO;
import com.aurionpro.app.service.InsuranceTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/app/insurancetypes")
@CrossOrigin(origins = "http://localhost:4200")
public class InsuranceTypeController {
	
	@Autowired
	private InsuranceTypeService insuranceTypeService;
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<InsuranceTypeResponseDTO> createInsuranceType(@RequestBody @Valid InsuranceTypeRequestDTO requestDto) {
		return ResponseEntity.ok(insuranceTypeService.createInsuranceType(requestDto));
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'AGENT', 'CUSTOMER')")
	public ResponseEntity<List<InsuranceTypeResponseDTO>> getAllInsuranceTypes() {
		return ResponseEntity.ok(insuranceTypeService.getAllInsuranceTypes());
	}
	
	@GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'AGENT', 'CUSTOMER')")
    public InsuranceTypeResponseDTO getInsuranceTypeById(@PathVariable int id) {
        return insuranceTypeService.getInsuranceTypeById(id);
    }
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public InsuranceTypeResponseDTO updateInsuranceType(@PathVariable int id,
	                                                    @RequestBody InsuranceTypeRequestDTO dto) {
		return insuranceTypeService.updateInsuranceType(id, dto);
	}
	 
	@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteInsuranceType(@PathVariable int id) {
        insuranceTypeService.deleteInsuranceType(id);
    }
}
