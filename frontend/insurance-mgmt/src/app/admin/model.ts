export interface StateResponseDto {
    stateId: number;
    stateName: string;
}

export interface CityResponseDto {
    cityId: number;
    cityName: string;
    stateId: number;
    stateName: string;
}

export interface InsuranceTypeResponseDTO {
    insuranceTypeId: number;
    name: string;
}

export interface InsurancePlanResponseDTO {
    insurancePlanId: number;
    planName: string;
    insuranceTypeName: string;
    insuranceTypeId: number;
    minCoverageAmount: number;
    maxCoverageAmount: number;
    minDurationYears: number;
    maxDurationYears: number;
    premiumRatePerThousandPerYear: number;
    description: string;
    commissionRate: number;
    isActive: boolean;
    requiredDocuments: string[];
}

// export enum DocumentType {
//     AADHAR = 'AADHAR',
//     PAN = 'PAN',
//     PASSPORT = 'PASSPORT'
// }

export interface PageResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    size: number;
    last: boolean;
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

export interface PlanPurchaseReportDto {
    policyId: number;
    customerName: string;
    phoneNumber: string;
    insuranceType: string;
    planName: string;
    startDate: string;
    endDate: string;
    calculatedPremium: number;
    paymentFrequency: 'MONTHLY' | 'YEARLY' | 'QUATERLY' | 'HALF_YEARLY';
    agentName: string;
    status: string;
}

export interface TransactionResponse {
    transactionId: number;
    amount: number;
    description: string;
    transactionType: string;
    transactionTime: string;
    userFullName: string;
    userRole: string;
}

export interface CustomerQueryResponse {
    id: number;
    subject: string;
    message: string;
    status: string;
    adminResponse: string;
    createdAt: Date;
    updatedAt: Date;
    customerName: string;
}

export interface PaginatedResponse<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    pageSize: number;
    last: boolean;
}

export interface AdminProfileDto {
    username: string;
    currentPassword: string;
    newPassword: string;
    email: string;
    firstName: string;
    lastName: string;
    role: string;
  }