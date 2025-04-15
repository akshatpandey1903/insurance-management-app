import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';  // Use the proper environment URL

@Injectable({
  providedIn: 'root'
})
export class InsurancePlanService {

  private apiUrl = 'http://localhost:8080/app/insuranceplans';  // URL for getting all insurance plans

  constructor(private http: HttpClient) { }

  private getHeaders() {
    const token = localStorage.getItem('accessToken');  // Fetch token from localStorage
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`  // Add token in the Authorization header
    });
  }

  // Get all insurance plans
  getAllPlans(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}`, { headers: this.getHeaders() });
  }
}
