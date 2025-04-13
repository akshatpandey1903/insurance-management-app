export interface PolicyClaimResponseDTO {
    id: number;
    policyId: number;
    planName: string;
    reason: string;
    status: string;
    remarks: string;
    verifiedBy: string;
    requestedAt: string;
    isEarlyClaim: boolean;
    penaltyAmount: number;
    claimAmount: number;
  }
  
  export interface ClaimFilterRequestDTO {
    status?: string;
    customerName?: string;
    fromDate?: string;
    toDate?: string;
  }
  
  export interface ClaimApprovalRequestDTO {
    claimId: number;
    employeeId: number;
    status: 'APPROVED' | 'REJECTED';
    remarks: string;
  }
  