import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CityResponseDto, StateResponseDto } from '../model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl= "http://localhost:8080/app";

  constructor(private httpClient : HttpClient) { }

  createState(stateData: any): Observable<StateResponseDto> {
    return this.httpClient.post<StateResponseDto>(`${this.apiUrl}/states`, stateData);
  }

  getAllStates(): Observable<StateResponseDto[]>{
    return this.httpClient.get<StateResponseDto[]>(`${this.apiUrl}/states`);
  }

  deleteState(id:number): Observable<void>{
    return this.httpClient.delete<void>(`${this.apiUrl}/states/${id}`);
  }

  createCity(cityData: any): Observable<CityResponseDto> {
    return this.httpClient.post<CityResponseDto>(`${this.apiUrl}/cities`, cityData); 
  }

  getCitiesByStateId(stateId: number): Observable<CityResponseDto[]> {
    return this.httpClient.get<CityResponseDto[]>(`${this.apiUrl}/cities/${stateId}`);
  }

  deleteCity(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiUrl}/cities/${id}`);
  }
  
}
