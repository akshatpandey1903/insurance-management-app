import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AgentProfileDTO, CustomerAndPolicyDTO, InsurancePlan } from '../model';

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  private baseUrl = 'http://localhost:8080/app/agent';

  constructor(private http: HttpClient) {}

  getProfile(agentId: number) {
    return this.http.get<AgentProfileDTO>(`${this.baseUrl}/profile?agentId=${agentId}`);
  }


}
