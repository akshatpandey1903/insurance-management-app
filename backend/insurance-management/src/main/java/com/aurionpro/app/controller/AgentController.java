package com.aurionpro.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.AgentAssignedPolicyDTO;
import com.aurionpro.app.dto.AgentProfileDTO;
import com.aurionpro.app.dto.AgentResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.service.AgentService;

@RestController
@RequestMapping("/app/agent")
public class AgentController {
	
	@Autowired
	private AgentService agentService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/pending")
	public ResponseEntity<PageResponse<AgentResponseDTO>> getPendingAgents(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "10") int size) {
	    return ResponseEntity.ok(agentService.getPendingAgents(page, size));
	}
	
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	@PutMapping("/approve/{agentId}")
	public ResponseEntity<AgentResponseDTO> approveAgent(@PathVariable int agentId, @RequestParam int approverId){
		return ResponseEntity.ok(agentService.approveAgent(agentId, approverId));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public void softDeleteAgent(@PathVariable int id){
		agentService.softDeleteAgent(id);
	}
	
	@PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
	@GetMapping("/{agentId}/assigned-policies")
	public ResponseEntity<List<AgentAssignedPolicyDTO>> getAssignedPolicies(@PathVariable int agentId) {
	    return ResponseEntity.ok(agentService.getAssignedPolicies(agentId));
	}
	
	@GetMapping("/profile")
	@PreAuthorize("hasRole('AGENT')")
	public AgentProfileDTO viewProfile(@RequestParam int agentId) {
	    return agentService.getProfile(agentId);
	}
}
