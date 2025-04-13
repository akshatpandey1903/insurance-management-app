import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageResponse } from '../../../models/page.model';
import { Observable, map } from 'rxjs';
import { CustomerDocumentResponseDTO, DocumentStatusUpdateRequestDTO } from '../../../models/document.model';

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  private apiUrl = `http://localhost:8080/app/documents`;

  constructor(private http: HttpClient) { }

  approveOrRejectDocument(request: DocumentStatusUpdateRequestDTO, employeeId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/approve/${employeeId}`, request);
  }
  
  getPendingDocuments(page: number, size: number): Observable<PageResponse<CustomerDocumentResponseDTO>> {
    const token = localStorage.getItem('accessToken');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
    
    // Send a zero-based page index to the backend
    return this.http.get<any>(`${this.apiUrl}/pending?page=${page}&size=${size}`, { headers })
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
}