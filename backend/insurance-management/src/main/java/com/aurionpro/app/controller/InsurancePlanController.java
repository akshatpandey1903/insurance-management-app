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

import com.aurionpro.app.dto.InsurancePlanRequestDTO;
import com.aurionpro.app.dto.InsurancePlanResponseDTO;
import com.aurionpro.app.service.InsurancePlanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/app/insuranceplans")
@CrossOrigin(origins = "http://localhost:4200")
public class InsurancePlanController {
	
	@Autowired
	private InsurancePlanService planService;
	
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<InsurancePlanResponseDTO> creatPlan(@RequestBody @Valid InsurancePlanRequestDTO requestDto){
		return ResponseEntity.ok(planService.createPlan(requestDto));
	}
	
	@GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'AGENT', 'CUSTOMER')")
    public ResponseEntity<List<InsurancePlanResponseDTO>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'AGENT', 'CUSTOMER')")
    public ResponseEntity<InsurancePlanResponseDTO> getPlanById(@PathVariable int id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InsurancePlanResponseDTO> updatePlan(@PathVariable int id, @RequestBody InsurancePlanRequestDTO dto) {
        return ResponseEntity.ok(planService.updatePlan(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePlan(@PathVariable int id) {
    	planService.deletePlan(id);
    }
}
