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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.StateRequestDTO;
import com.aurionpro.app.dto.StateResponseDTO;
import com.aurionpro.app.service.StateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app/states")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class StateController {
	
	@Autowired
	private StateService stateService;
	
	@PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<StateResponseDTO> createState(@RequestBody StateRequestDTO stateDto) {
        return ResponseEntity.ok(stateService.addState(stateDto));
    }
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'AGENT', 'CUSTOMER')")
    public ResponseEntity<List<StateResponseDTO>> getAllStates() {
        return ResponseEntity.ok(stateService.getAllStates());
    }
	
	@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteState(@PathVariable int id) {
        stateService.deleteState(id);
        return ResponseEntity.noContent().build();
    }
	
}
