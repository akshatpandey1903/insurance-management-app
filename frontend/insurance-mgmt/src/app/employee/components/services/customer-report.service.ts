// src/app/modules/employee/services/customer-report.service.ts
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../models/page.model';
import { CustomerReportDto } from '../../../models/customer.model';

@Injectable({ providedIn: 'root' })
export class CustomerReportService {
  private baseUrl = 'http://localhost:8080/app/reports/customers';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken') || '';
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getCustomerReports(page: number, size: number): Observable<PageResponse<CustomerReportDto>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size);
    return this.http.get<PageResponse<CustomerReportDto>>(this.baseUrl, {
      headers: this.getHeaders(),
      params
    });
  }
}
