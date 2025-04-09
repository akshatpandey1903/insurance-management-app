import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CityResponseDto, InsurancePlanResponseDTO, InsuranceTypeResponseDTO, StateResponseDto } from '../model';
import { access } from 'fs';
import { AuthService } from '../../services/auth.service';
import { log } from 'console';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl= "http://localhost:8080/app";

  constructor(private httpClient : HttpClient , private authService : AuthService) { }

  // Api call for states

  createState(stateData: any): Observable<StateResponseDto> {
    
    return this.httpClient.post<StateResponseDto>(`${this.apiUrl}/states`, stateData);
  }

  getAllStates(): Observable<StateResponseDto[]>{
    return this.httpClient.get<StateResponseDto[]>(`${this.apiUrl}/states`);
  }

  deleteState(id:number): Observable<void>{
    return this.httpClient.delete<void>(`${this.apiUrl}/states/${id}`);
  }

  // Api call for cities

  createCity(cityData: any): Observable<CityResponseDto> {
    return this.httpClient.post<CityResponseDto>(`${this.apiUrl}/cities`, cityData); 
  }

  getCitiesByStateId(stateId: number): Observable<CityResponseDto[]> {
    return this.httpClient.get<CityResponseDto[]>(`${this.apiUrl}/cities/${stateId}`);
  }

  deleteCity(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiUrl}/cities/${id}`);
  }
  
  // Api call for insurance type

  createInsuranceType(typeData: any): Observable<InsuranceTypeResponseDTO>
  {
    const headers = this.getAuthHeaders();
    return this.httpClient.post<InsuranceTypeResponseDTO>(`${this.apiUrl}/insurancetypes`,typeData , { headers });
  }

  getAllInsuranceTypes(): Observable<InsuranceTypeResponseDTO[]> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<InsuranceTypeResponseDTO[]>(`${this.apiUrl}/insurancetypes` , { headers });
  }

  getAllInsuranceTypesById(id: number): Observable<InsuranceTypeResponseDTO> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<InsuranceTypeResponseDTO>(`${this.apiUrl}/insurancetypes/${id}`,{ headers });
  }

  updateInsuranceType(id:number , insuranceType: InsuranceTypeResponseDTO): Observable<InsuranceTypeResponseDTO>
  {
    const headers = this.getAuthHeaders();
    return this.httpClient.put<InsuranceTypeResponseDTO>(`${this.apiUrl}/insurancetypes/${id}`,insuranceType,{ headers });
  }

  deleteInsuranceType(id:number): Observable<void>
  {
    const headers = this.getAuthHeaders();
    return this.httpClient.delete<void>(`${this.apiUrl}/insurancetypes/${id}`,{ headers })
  }

  // Api calls for Insurance plan

  createInsurancePlan(planData: any): Observable<InsurancePlanResponseDTO> {
    const headers = this.getAuthHeaders();
    return this.httpClient.post<InsurancePlanResponseDTO>(`${this.apiUrl}/insuranceplans`, planData , { headers });
  }

  getAllInsurancePlans(): Observable<InsurancePlanResponseDTO[]> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<InsurancePlanResponseDTO[]>(`${this.apiUrl}/insuranceplans`, {headers});
  }

  getPlansById(typeId: number): Observable<InsurancePlanResponseDTO[]> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<InsurancePlanResponseDTO[]>(`${this.apiUrl}/insuranceplans/${typeId}`,{headers});
  }

  updateInsurancePlan(id:number , insurancePlan: InsurancePlanResponseDTO): Observable<InsurancePlanResponseDTO>
  {
    const headers = this.getAuthHeaders();
    return this.httpClient.put<InsurancePlanResponseDTO>(`${this.apiUrl}/insuranceplans/${id}`,insurancePlan,{headers});
  }

  deleteInsurancePlan(id: number): Observable<void> {
    // const headers = this.getAuthHeaders();
    return this.httpClient.delete<void>(`${this.apiUrl}/insuranceplans/${id}`);
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // create new customer

  // createUser(userData: any): Observable<any> {
  //   console.log("Access Token is")
  //   console.log(localStorage.getItem('accessToken'))
  //   const role = this.authService.getRoleName()
  //   console.log(role)
  //   return this.httpClient.post<any>(`${this.apiUrl}/register`, userData );
  // }

  createUser(userData: any): Observable<any> {
    // const currentRole = this.authService.getRoleName(); // Use getRoleName here
    // console.log('Current role:', currentRole); // Debug log
    // if (currentRole !== 'ADMIN') {
    //   throw new Error('Only admins can create users');
    // }
    const headers = this.getAuthHeaders();
    return this.httpClient.post<any>(`${this.apiUrl}/register/customer`, userData, { headers });
  }

}
