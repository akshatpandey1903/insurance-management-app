import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PolicyService {
  private apiUrl = 'http://localhost:8080/app/policy/register'
  constructor(private http: HttpClient) { }

  private getHeaders() {
    const token = localStorage.getItem('token'); 
    return new HttpHeaders({
      'Authorization': `Bearer ${token}` 
    });
  }

  registerPolicy(data: any, customerId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${customerId}`, data, { headers: this.getHeaders() });
  }
}
