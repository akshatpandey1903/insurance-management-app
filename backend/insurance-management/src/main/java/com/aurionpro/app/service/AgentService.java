package com.aurionpro.app.service;

import java.util.List;

import com.aurionpro.app.dto.AgentAssignedPolicyDTO;
import com.aurionpro.app.dto.AgentProfileDTO;
import com.aurionpro.app.dto.AgentResponseDTO;
import com.aurionpro.app.dto.PageResponse;

public interface AgentService {
	PageResponse<AgentResponseDTO> getPendingAgents(int pageNumber, int pageSize);
    AgentResponseDTO approveAgent(int agentId, int approverId);
    void softDeleteAgent(int id);
    List<AgentAssignedPolicyDTO> getAssignedPolicies(int agentId);
    AgentProfileDTO getProfile(int agentId);
    AgentResponseDTO rejectAgent(int agentId, int approverId);
}
