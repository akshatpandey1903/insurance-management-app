import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../models/page.model';
import { AgentReportResponseDTO } from '../../../models/agent.model';
import { AgentCommissionReportDto } from '../../../models/agent.model';

@Injectable({
  providedIn: 'root'
})
export class AgentReportService {
  private baseUrl = 'http://localhost:8080/app/admin/reports';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken') || '';
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getAgentReports(page: number, size: number): Observable<PageResponse<AgentReportResponseDTO>> {
    const params = new HttpParams()
      .set('pageNumber', page)
      .set('pageSize', size);
    return this.http.get<PageResponse<AgentReportResponseDTO>>(`${this.baseUrl}/agents`, {
      headers: this.getHeaders(),
      params
    });
  }

  getAgentCommissionReport(page: number = 0, size: number = 10): Observable<PageResponse<AgentCommissionReportDto>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);

    return this.http.get<PageResponse<AgentCommissionReportDto>>(`${this.baseUrl}/agent-commissions`, { params });
  }
}
