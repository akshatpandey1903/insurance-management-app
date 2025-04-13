import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../models/page.model';
import { CustomerQueryResponseDTO } from '../../../models/customer.model';

@Injectable({
  providedIn: 'root'
})
export class QueryService {
  private baseUrl = 'http://localhost:8080/app/admin/queries';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken') || '';
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getAllQueries(page: number, size: number, status?: string): Observable<PageResponse<CustomerQueryResponseDTO>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<PageResponse<CustomerQueryResponseDTO>>(this.baseUrl, {
      headers: this.getHeaders(),
      params
    });
  }

  respondToQuery(queryId: number, responseText: string): Observable<CustomerQueryResponseDTO> {
    return this.http.put<CustomerQueryResponseDTO>(
      `${this.baseUrl}/${queryId}/response`,
      responseText,
      {
        headers: this.getHeaders()
          .set('Content-Type', 'text/plain')
      }
    );
  }
}
