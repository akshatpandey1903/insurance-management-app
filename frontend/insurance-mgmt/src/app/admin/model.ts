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
    insuranceTypeId: number;
    planName: string;
    insuranceTypeName: string;
    yearlyPremiumAmount: number;
    coverageAmount: number;
    durationYears: number;
    description: string;
    commissionRate: number;
    isActive: boolean;
}

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
    paymentFrequency: string;
    agentName: string;
    status: string;
}

export interface TransactionResponse 
{
    transactionId: number;
    amount: number;
    description: string;
    transactionType: string;
    transactionTime: string;
    userFullName: string;
    userRole: string;
}
