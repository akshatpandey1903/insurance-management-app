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
	public PageResponse<AgentCommissionReportDto> getAgentCommissionReport(Pageable pageable, String keyword) {
	    Page<AgentCommissionReportDto> page;

	    if (keyword == null || keyword.isBlank()) {
	        page = customerPolicyRepository.getAgentCommissionReport(pageable);
	    } else {
	        page = customerPolicyRepository.searchAgentCommissionReport(keyword, pageable);
	    }

	    return new PageResponse<>(
	        page.getContent(),
	        page.getTotalPages(),
	        page.getTotalElements(),
	        page.getSize(),
	        page.isLast()
	    );
	}

    
	@Override
	public PageResponse<AgentReportResponseDTO> getAgentReport(int pageNumber, int pageSize, String search) {
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
	    Page<AgentReportResponseDTO> page;
	    
	    if (search != null && !search.isBlank()) {
	        page = agentRepository.searchAgentReportByKeyword(search, pageable);
	    } else {
	        page = agentRepository.getAgentReport(pageable);
	    }
	    
	    return new PageResponse<>(
	        page.getContent(),
	        page.getTotalPages(),
	        page.getTotalElements(),
	        page.getSize(),
	        page.isLast()
	    );
	}

}
