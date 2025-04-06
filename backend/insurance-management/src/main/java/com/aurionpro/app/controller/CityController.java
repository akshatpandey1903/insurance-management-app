package com.aurionpro.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.CityRequestDTO;
import com.aurionpro.app.dto.CityResponseDTO;
import com.aurionpro.app.service.CityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {
	
	@Autowired
	private CityService cityService;
	
	@PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CityResponseDTO> createCity(@RequestBody CityRequestDTO cityDto) {
        return ResponseEntity.ok(cityService.addCity(cityDto));
    }
	
	@GetMapping("/{stateId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'AGENT', 'CUSTOMER')")
    public ResponseEntity<List<CityResponseDTO>> getAllCities(@PathVariable int stateId) {
        return ResponseEntity.ok(cityService.getCitiesByStateId(stateId));
    }
	
	@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCity(@PathVariable int id) {
        cityService.deleteCity(id);
        return ResponseEntity.noContent().build();
    }
}
