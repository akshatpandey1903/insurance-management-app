package com.aurionpro.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.AgentCommissionReportDto;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.repository.CustomerPolicyRepository;

@Service
public class AgentReportServiceImpl implements AgentReportService {

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
}
