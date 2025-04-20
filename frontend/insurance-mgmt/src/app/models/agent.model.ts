export interface AgentResponseDTO {
    userId: number;
    fullName: string;
    email: string;
    licenseNumber: string;
    isApproved: boolean;
  }
  

export interface AgentReportResponseDTO {
    agentId: number;
    name: string;
    email: string;
    approvedBy: string;
    totalPoliciesRegistered: number;
    totalCommissionEarnedYearly: number;
    registeredAt: string; 
  }
  

export interface AgentCommissionReportDto {
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
    totalPoliciesSold: number;
    totalCommissionRate: number;
    totalEarnings: number;
  }
  
export interface AgentUpdateRequest {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  newPassword?: string;
}
