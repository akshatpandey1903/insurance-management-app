package com.aurionpro.app.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.AgentResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.Agent;
import com.aurionpro.app.entity.Employee;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.AgentRepository;
import com.aurionpro.app.repository.EmployeeRepository;

@Service
public class AgentServiceImpl implements AgentService{
	
	@Autowired
    private AgentRepository agentRepository;
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	private ModelMapper modelMapper;
	
	public AgentServiceImpl() {
		this.modelMapper = new ModelMapper();
	}

	@Override
	public PageResponse<AgentResponseDTO> getPendingAgents(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
	    Page<Agent> page = agentRepository.findByIsApprovedFalseAndIsActiveTrue(pageable);

	    List<AgentResponseDTO> content = page.getContent()
	        .stream()
	        .map(agent -> new AgentResponseDTO(
	            agent.getUserId(),
	            agent.getFirstName() + " "  + agent.getLastName(),
	            agent.getEmail(),
	            agent.getLicenseNumber(),
	            agent.isApproved()
	        ))
	        .toList();

	    return new PageResponse<>(
	        content,
	        page.getTotalPages(),
	        page.getTotalElements(),
	        page.getSize(),
	        page.isLast()
	    );
	}

	@Override
	public AgentResponseDTO approveAgent(int agentId, int approverId) {
		Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));
		
		if(!agent.isActive()) {
			throw new IllegalStateException("Agent is inactive");
		}
		
        if (agent.isApproved()) {
            throw new IllegalStateException("Agent is already approved");
        }

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Approver not found"));

        agent.setApproved(true);
        agent.setApprovedBy(approver);

        Agent updated = agentRepository.save(agent);
        AgentResponseDTO response = new AgentResponseDTO(
        		updated.getUserId(),
        		updated.getFirstName() + " "  + agent.getLastName(),
        		updated.getEmail(),
        		updated.getLicenseNumber(),
        		updated.isApproved()
        		);
        return response;
	}

	@Override
	public void softDeleteAgent(int id) {
		Agent agent = agentRepository.findById(id)
		        .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));
		    agent.setActive(false);;
		    agentRepository.save(agent);
	}

}
