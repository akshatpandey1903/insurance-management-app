import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserResponseDTO } from '../../../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private baseUrl = 'http://localhost:8080/app/employee/profile';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken') || '';
    return new HttpHeaders({ Authorization: `Bearer ${token}` });
  }

  getProfile(): Observable<UserResponseDTO> {
    const employeeId = localStorage.getItem('userId');
    return this.http.get<UserResponseDTO>(`${this.baseUrl}/${employeeId}`, {
      headers: this.getHeaders()
    });
  }

  updateProfile(data: {
    username: string;
    password: string;
    firstName: string;
    lastName: string;
  }): Observable<UserResponseDTO> {
    return this.http.put<UserResponseDTO>(`${this.baseUrl}/update`, data, {
      headers: this.getHeaders()
    });
  }
}
