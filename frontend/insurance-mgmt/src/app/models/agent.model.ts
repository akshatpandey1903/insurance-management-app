export interface AgentResponseDTO {
    userId: number;
    fullName: string;
    email: string;
    licenseNumber: string;
    isApproved: boolean;
  }
  
  // src/app/models/agent.model.ts

export interface AgentReportResponseDTO {
    agentId: number;
    name: string;
    email: string;
    approvedBy: string;
    totalPoliciesRegistered: number;
    totalCommissionEarnedYearly: number;
    registeredAt: string; // or Date if you parse it
  }
  
  // src/app/models/agent-commission-report.model.ts

export interface AgentCommissionReportDto {
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
    totalPoliciesSold: number;
    totalCommissionRate: number;
    totalEarnings: number;
  }
  