import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../models/page.model';
import { ClaimApprovalRequestDTO, ClaimFilterRequestDTO, PolicyClaimResponseDTO } from '../../../models/claim.model';

@Injectable({
  providedIn: 'root'
})
export class ClaimService {
  private baseUrl = 'http://localhost:8080/app/customer/claims';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken') || '';
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getPendingClaims(page: number, size: number): Observable<PageResponse<PolicyClaimResponseDTO>> {
    const headers = this.getAuthHeaders();
    const filter: ClaimFilterRequestDTO = { status: 'PENDING' };

    return this.http.post<PageResponse<PolicyClaimResponseDTO>>(
      `${this.baseUrl}/all?pageNumber=${page}&pageSize=${size}`,
      filter,
      { headers }
    );
  }

  approveOrRejectClaim(request: ClaimApprovalRequestDTO): Observable<PolicyClaimResponseDTO> {
    const headers = this.getAuthHeaders();
    return this.http.put<PolicyClaimResponseDTO>(`${this.baseUrl}/approve`, request, { headers });
  }
}
