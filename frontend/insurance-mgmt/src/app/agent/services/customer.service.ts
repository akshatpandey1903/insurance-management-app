import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl = 'http://localhost:8080/app/customers';  // URL for getting all customers

  constructor(private http: HttpClient) { }

  private getHeaders() {
    const token = localStorage.getItem('accessToken');  // Fetch token from localStorage
    return new HttpHeaders({
      'Authorization': `Bearer ${token}` 
    });
  }

  getAllCustomers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`, { headers: this.getHeaders() });
  }
}
