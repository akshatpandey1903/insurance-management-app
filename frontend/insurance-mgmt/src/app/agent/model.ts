export interface AgentProfileDTO {
  id: number;
  username: string,
  firstName: string;
  lastName: string;
  email: string;
  approved: boolean;
  createdAt: Date;
  approvedBy: string;
  active: boolean,
  licenseNumber: number
}

// insurance-plan.model.ts
export interface InsurancePlan {
  id: number;
  planName: string;
  premium: number;
  coverageAmount: number;
  durationYears: number;
  commissionRate: number;
}

// customer-registration.dto.ts
export interface CustomerRegistrationDTO {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  address: string;
  phoneNumber: string;
  active: boolean;
}

// customer-policy-request.dto.ts
export interface CustomerPolicyRequestDTO {
  insurancePlanId: number;
  paymentFrequency: string;
  selectedDurationYears: number;
  selectedCoverageAmount: number;
  licenseNumber?: string;
}

// combined payload
export interface CustomerAndPolicyDTO {
  customerDTO: CustomerRegistrationDTO;
  policyDTO: CustomerPolicyRequestDTO;
}
