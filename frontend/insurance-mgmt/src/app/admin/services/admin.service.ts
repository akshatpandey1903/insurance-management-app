import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Inject, Injectable, PLATFORM_ID} from '@angular/core';
import { Observable } from 'rxjs';
import { AdminProfileDto, AgentCommissionReportDto, CityResponseDto, InsurancePlanResponseDTO, InsuranceTypeResponseDTO, PageResponse, PaginatedResponse, PlanPurchaseReportDto, StateResponseDto, TransactionResponse } from '../model';
import { AuthService } from '../../services/auth.service';
import { CustomerReportComponent } from '../components/reports/customer-report/customer-report.component';
import { CustomerQueryResponse } from '../../customer/model';
import { isPlatformBrowser } from '@angular/common';
import { Page } from 'ngx-pagination';
import { AgentReportResponseDTO } from '../../models/agent.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private apiUrl = "http://localhost:8080/app";
  private isBrowser: boolean;

  constructor(private httpClient: HttpClient, private authService: AuthService ,  @Inject(PLATFORM_ID) private platformId: Object) { this.isBrowser = isPlatformBrowser(this.platformId);}

  // Api call for insurance type

  createInsuranceType(typeData: any): Observable<InsuranceTypeResponseDTO> {
    const headers = this.getAuthHeaders();
    return this.httpClient.post<InsuranceTypeResponseDTO>(`${this.apiUrl}/insurancetypes`, typeData, { headers });
  }

  getAllInsuranceTypes(): Observable<InsuranceTypeResponseDTO[]> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<InsuranceTypeResponseDTO[]>(`${this.apiUrl}/insurancetypes`, { headers });
  }

  getAllInsuranceTypesById(id: number): Observable<InsuranceTypeResponseDTO> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<InsuranceTypeResponseDTO>(`${this.apiUrl}/insurancetypes/${id}`, { headers });
  }

  updateInsuranceType(id: number, insuranceType: InsuranceTypeResponseDTO): Observable<InsuranceTypeResponseDTO> {
    const headers = this.getAuthHeaders();
    return this.httpClient.put<InsuranceTypeResponseDTO>(`${this.apiUrl}/insurancetypes/${id}`, insuranceType, { headers });
  }

  deleteInsuranceType(id: number): Observable<void> {
    const headers = this.getAuthHeaders();
    return this.httpClient.delete<void>(`${this.apiUrl}/insurancetypes/${id}`, { headers })
  }

  // Api calls for Insurance plan

  // createInsurancePlan(planData: any): Observable<InsurancePlanResponseDTO> {
  //   const headers = this.getAuthHeaders();
  //   return this.httpClient.post<InsurancePlanResponseDTO>(`${this.apiUrl}/insuranceplans/`, planData, { headers });
  // }

  createInsurancePlan(planData: any): Observable<InsurancePlanResponseDTO> {
    const headers = this.getAuthHeaders();
    const role = this.getRoleName();
    console.log("Role at time of plan creation:", role);
    if (role !== 'ADMIN') {
      throw new Error('Unauthorized: Only admins can create insurance plans.');
    }
  
    return this.httpClient.post<InsurancePlanResponseDTO>(`${this.apiUrl}/insuranceplans`, planData, { headers });
  }

  getRoleName(): string | null {
    const token = localStorage.getItem('accessToken');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const authorities = payload.authorities || payload.role || [];
      if (Array.isArray(authorities) && authorities.length > 0) {
        // Assuming first authority is what we want
        const roleWithPrefix = authorities[0].authority;
        return roleWithPrefix.replace('ROLE_', '');
      }
    }
    return null;
  }


  getAllInsurancePlans(): Observable<InsurancePlanResponseDTO[]> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<InsurancePlanResponseDTO[]>(`${this.apiUrl}/insuranceplans`, { headers });
  }

  getPlansById(typeId: number): Observable<InsurancePlanResponseDTO[]> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<InsurancePlanResponseDTO[]>(`${this.apiUrl}/insuranceplans/${typeId}`, { headers });
  }

  updateInsurancePlan(id: number, insurancePlan: InsurancePlanResponseDTO): Observable<InsurancePlanResponseDTO> {
    const headers = this.getAuthHeaders();
    return this.httpClient.put<InsurancePlanResponseDTO>(`${this.apiUrl}/insuranceplans/${id}`, insurancePlan, { headers });
  }

  deleteInsurancePlan(id: number): Observable<void> {
    // const headers = this.getAuthHeaders();
    return this.httpClient.delete<void>(`${this.apiUrl}/insuranceplans/${id}`);
  }

  private getAuthHeaders(): HttpHeaders {
    let token = '';
    if (this.isBrowser) {
      token = localStorage.getItem('accessToken') || '';
    }

    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }
  // create new customer

  createUser(userData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.httpClient.post<any>(`${this.apiUrl}/register/customer`, userData, { headers });
  }

  getCustomerReports(page: number = 0, size: number = 10): Observable<PageResponse<CustomerReportComponent>> {
    return this.httpClient.get<PageResponse<CustomerReportComponent>>(
      `http://localhost:8080/app/reports/customers?page=${page}&size=${size}`
    );
  }

  getAllAgents(pageNumber: number, pageSize: number, keyword: string = ''): Observable<PageResponse<AgentReportResponseDTO>> {
    return this.httpClient.get<PageResponse<AgentReportResponseDTO>>(`${this.apiUrl}/admin/reports/agents?pageNumber=${pageNumber}&pageSize=${pageSize}&keyword=${keyword}`);
  }

  getAgentCommissionReport(page: number, size: number, keyword: string = ''): Observable<PageResponse<AgentCommissionReportDto>> {
    return this.httpClient.get<PageResponse<AgentCommissionReportDto>>(
      `${this.apiUrl}/admin/reports/agent-commissions?page=${page}&size=${size}&keyword=${keyword}`
    );
  }

  getPolicyPayments(page: number, size: number): Observable<PageResponse<PlanPurchaseReportDto>> {

    return this.httpClient.get<PageResponse<PlanPurchaseReportDto>>(`${this.apiUrl}/admin/reports/plan-purchases?page=${page}&size=${size}`);
  }

  getTransactionReports(page: number, size: number): Observable<PageResponse<TransactionResponse>> {
    return this.httpClient.get<PageResponse<TransactionResponse>>(
      `${this.apiUrl}/transactions?page=${page}&size=${size}`
    );
  }

  resolveQuery(queryId: number, response: string): Observable<any> {
    return this.httpClient.put(`http://localhost:8080/app/admin/queries/${queryId}/response`, response, {
      responseType: 'text'  // or json if you're returning DTO
    });
  }

  getUnresolvedQueries(): Observable<PaginatedResponse<CustomerQueryResponse>> {
    return this.httpClient.get<PaginatedResponse<CustomerQueryResponse>>(`http://localhost:8080/app/admin/queries`);

  }

  // Profile

  getAdminProfile(): Observable<AdminProfileDto>{
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
    return this.httpClient.get<AdminProfileDto>(`${this.apiUrl}/admin/profile` , {headers});
  }

  updateAdminProfile(payload: any): Observable<string> {
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
    return this.httpClient.put(`${this.apiUrl}/admin/profile`, payload, { headers, responseType: 'text' });
  }

}
