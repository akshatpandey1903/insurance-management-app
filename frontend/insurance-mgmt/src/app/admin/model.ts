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