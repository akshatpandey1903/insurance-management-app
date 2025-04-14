export interface CustomerPolicyResponseDTO {
    id: number;
    customerName: string;
    insurancePlanName: string;
    paymentFrequency: string;
    calculatedPremium: number;
    selectedCoverageAmount: number;
    selectedDurationYears: number;
    startDate: string | null;
    endDate: string | null;
    nextDueDate: string | null;
    active: boolean;
    approvedBy: string | null;
    agentName: string;
  }
  

  export interface CustomerQueryResponseDTO {
    id: number;
    subject: string;
    message: string;
    status: string; // PENDING, RESPONDED
    adminResponse: string | null;
    createdAt: string;
    updatedAt: string;
    customerName: string;
  }

export interface CustomerReportDto {
    userId: number;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    totalPolicies: number;
  }
  
  export interface CustomerPolicyRequestDTO {
    insurancePlanId: number;
    paymentFrequency: 'MONTHLY' | 'QUARTERLY' | 'HALF_YEARLY' | 'YEARLY'; // match your enum from backend
    selectedDurationYears: number;
    selectedCoverageAmount: number;
    licenseNumber?: string; // optional
  }
  