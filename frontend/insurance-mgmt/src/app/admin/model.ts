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

export interface InsuranceTypeResponseDTO{
    insuranceTypeId: number;
    name: string;
}

export interface InsurancePlanResponseDTO{
    insurancePlanId: number;
    insuranceTypeId: number;
    planName: string;
    insuranceTypeName: string;
    yearlyPremiumAmount: number;
    coverageAmount: number;
    durationYears: number;
    description: string;
    commissionRate: number;
    isActive : boolean;
}
