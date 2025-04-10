import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AgentCommissionReportDto, CityResponseDto, InsurancePlanResponseDTO, InsuranceTypeResponseDTO, PageResponse, PlanPurchaseReportDto, StateResponseDto, TransactionResponse } from '../model';
import { access } from 'fs';
import { AuthService } from '../../services/auth.service';
import { log } from 'console';
import { CustomerReportComponent } from '../components/reports/customer-report/customer-report.component';

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
    const headers = this.getAuthHeaders();
    return this.httpClient.post<any>(`${this.apiUrl}/register/customer`, userData, { headers });
  }

  getCustomerReports(page: number = 0, size: number = 10): Observable<PageResponse<CustomerReportComponent>> {
    return this.httpClient.get<PageResponse<CustomerReportComponent>>(
      `http://localhost:8080/app/reports/customers?page=${page}&size=${size}`
    );
  }

  getAllAgents(pageNumber: number, pageSize: number): Observable<any> {
    return this.httpClient.get(`${this.apiUrl}/admin/reports/agents?pageNumber=${pageNumber}&pageSize=${pageSize}`);
  }
  
  getAgentCommissionReport(page: number, size: number): Observable<PageResponse<AgentCommissionReportDto>> {
    return this.httpClient.get<PageResponse<AgentCommissionReportDto>>(
      `${this.apiUrl}/admin/reports/agent-commissions?page=${page}&size=${size}`
    );
  }

  getPolicyPayments(page: number, size: number): Observable<PageResponse<PlanPurchaseReportDto>> {

    return this.httpClient.get<PageResponse<PlanPurchaseReportDto>>(`${this.apiUrl}/admin/reports`);
  }

  getTransactionReports(page: number, size: number): Observable<PageResponse<TransactionResponse>> 
  { return this.httpClient.get<PageResponse<TransactionResponse>>( 
    `${this.apiUrl}/transactions?page=${page}&size=${size}` 
  ); 
}

}
