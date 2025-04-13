import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AgentResponseDTO } from '../../../models/agent.model';
import { PageResponse } from '../../../models/page.model';

@Injectable({
  providedIn: 'root'
})
export class AgentService {
  private baseUrl = 'http://localhost:8080/app'
  private apiUrl = `http://localhost:8080/app/agent`

  constructor(private http: HttpClient) { }

  registerAgent(agentData: any): Observable<any> {
    const token = localStorage.getItem('accessToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  
    return this.http.post(`${this.baseUrl}/register/agent`, agentData, { headers });
  }
  
  getPendingAgents(page: number, size: number): Observable<PageResponse<AgentResponseDTO>> {
    const token = localStorage.getItem('accessToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  
    return this.http.get<PageResponse<AgentResponseDTO>>(
      `${this.apiUrl}/pending?page=${page}&size=${size}`,
      { headers }
    );
  }  

  approveAgent(agentId: number): Observable<any> {
    const approverId = localStorage.getItem('userId');
    return this.http.put(`${this.apiUrl}/approve/${agentId}?approverId=${approverId}`, {});
  }

  rejectAgent(agentId: number): Observable<any> {
    const approverId = localStorage.getItem('userId');
    return this.http.put(`${this.apiUrl}/reject/${agentId}?approverId=${approverId}`, {});
  }
}
