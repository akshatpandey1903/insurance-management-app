package com.aurionpro.app.service;

import org.springframework.data.domain.Pageable;

import com.aurionpro.app.dto.AgentCommissionReportDto;
import com.aurionpro.app.dto.PageResponse;

public interface AgentReportService {
	PageResponse<AgentCommissionReportDto> getAgentCommissionReport(Pageable pageable);
}
