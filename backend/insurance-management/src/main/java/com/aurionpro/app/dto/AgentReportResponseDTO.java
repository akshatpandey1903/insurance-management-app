package com.aurionpro.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AgentReportResponseDTO {
    private int agentId;
    private String name;
    private String email;
    private String approvedBy;
    private int totalPoliciesRegistered;
    private BigDecimal totalCommissionEarnedYearly;
    private LocalDateTime registeredAt;
    
    public AgentReportResponseDTO(int agentId, String name, String email, String approvedBy, Long totalPoliciesRegistered, BigDecimal totalCommissionEarnedYearly, LocalDateTime registeredAt) 
    { 
    	this.agentId = agentId; 
    	this.name = name; 
    	this.email = email; 
    	this.approvedBy = approvedBy; 
    	this.totalPoliciesRegistered = totalPoliciesRegistered != null ? totalPoliciesRegistered.intValue() : 0; 
    	this.totalCommissionEarnedYearly = totalCommissionEarnedYearly; 
    	this.registeredAt = registeredAt;
    } 

}
