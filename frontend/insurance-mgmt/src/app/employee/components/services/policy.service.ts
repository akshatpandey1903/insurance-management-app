import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { PageResponse } from '../../../models/page.model';
import { CustomerPolicyResponseDTO } from '../../../models/customer.model';

@Injectable({
  providedIn: 'root'
})
export class PolicyService {
  private apiUrl = `http://localhost:8080/app/policy`;

  constructor(private http: HttpClient) { }

  getUnapprovedPolicies(page: number, size: number): Observable<PageResponse<CustomerPolicyResponseDTO>> {
    const token = localStorage.getItem('accessToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  
    return this.http.get<any>(`${this.apiUrl}/unapproved?page=${page}&size=${size}`, { headers })
      .pipe(
        map(response => ({
          content: response.content,
          totalPages: response.totalPages,
          totalElements: response.totalElements,
          size: response.pageSize, // Map pageSize to size
          isLast: response.last    // Map last to isLast
        }))
      );
  }
  
  approvePolicy(policyId: number): Observable<CustomerPolicyResponseDTO> {
    const token = localStorage.getItem('accessToken');
    const employeeId = localStorage.getItem('userId');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  
    return this.http.put<CustomerPolicyResponseDTO>(
      `${this.apiUrl}/approve/${policyId}?employeeId=${employeeId}`,
      {},
      { headers }
    );
  }
  
  rejectPolicy(policyId: number, reason: string): Observable<string> {
    const token = localStorage.getItem('accessToken');
    const employeeId = localStorage.getItem('userId');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  
    return this.http.put<string>(
      `${this.apiUrl}/reject/${policyId}?employeeId=${employeeId}&reason=${encodeURIComponent(reason)}`,
      {},
      { headers }
    );
  }
  
}
