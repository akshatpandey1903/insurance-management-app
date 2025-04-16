import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AgentUpdateRequest } from '../../models/agent.model';

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  private baseUrl = 'http://localhost:8080/app/agent';
  private accessToken = localStorage.getItem('accessToken');

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getCommissionReport(agentId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/earnings/summary/${agentId}`, {
      headers: this.getAuthHeaders()
    });
  }
  
  getEarningsDetails(agentId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/earnings/details/${agentId}`,{
      headers: this.getAuthHeaders()
    });
  }
  
  getAgentProfile(agentId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/profile/${agentId}`, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${this.accessToken}`
      })
    });
  }

  updateAgentProfile(agentId: number, updateData: AgentUpdateRequest): Observable<any> {
    return this.http.put(`${this.baseUrl}/profile/${agentId}`, updateData, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${this.accessToken}`
      })
    });
  }
}
