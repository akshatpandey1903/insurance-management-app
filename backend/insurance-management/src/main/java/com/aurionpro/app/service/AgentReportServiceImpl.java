package com.aurionpro.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.AgentCommissionReportDto;
import com.aurionpro.app.dto.AgentReportResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.Agent;
import com.aurionpro.app.repository.AgentRepository;
import com.aurionpro.app.repository.CustomerPolicyRepository;

@Service
public class AgentReportServiceImpl implements AgentReportService {
	
	@Autowired
	private AgentRepository agentRepository;

	@Autowired
    private CustomerPolicyRepository customerPolicyRepository;

    @Override
    public PageResponse<AgentCommissionReportDto> getAgentCommissionReport(Pageable pageable) {
        Page<AgentCommissionReportDto> page = customerPolicyRepository.getAgentCommissionReport(pageable);
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.isLast()
        );
    }
    
    @Override
    public PageResponse<AgentReportResponseDTO> getAgentReport(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        Page<Agent> page = agentRepository.findAll(pageable);

        List<AgentReportResponseDTO> report = page.getContent().stream().map(agent -> {
            AgentReportResponseDTO dto = new AgentReportResponseDTO();
            dto.setAgentId(agent.getUserId());
            dto.setName(agent.getFirstName() + " " + agent.getLastName());
            dto.setEmail(agent.getEmail());
            dto.setApprovedBy(agent.getApprovedBy() != null
                ? agent.getApprovedBy().getFirstName() + " " + agent.getApprovedBy().getLastName()
                : "Not Approved");
            dto.setRegisteredAt(agent.getCreatedAt());

            int totalPolicies = agent.getSoldPolicies() != null ? agent.getSoldPolicies().size() : 0;

            dto.setTotalPoliciesRegistered(totalPolicies);
            dto.setTotalCommissionEarnedYearly(agent.getTotalEarnings());

            return dto;
        }).toList();

        return new PageResponse<>(
            report,
            page.getTotalPages(),
            page.getTotalElements(),
            page.getSize(),
            page.isLast()
        );
    }

}
