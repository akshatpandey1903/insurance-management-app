import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/app';

  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, data)
  }

  register(data: any): Observable<any> {

    let endpoint = '';
    let payload: any = {
      username: data.username,
      firstName: data.firstName,
      lastName: data.lastName,
      email: data.email,
      password: data.password,
      role: data.role,
      address: data.address,
      phoneNumber: data.phoneNumber,
      department: data.department,
      designation: data.designation,
      licenseNumber: data.licenseNumber
    };

    switch (data.role.toUpperCase()) {
      case 'CUSTOMER':
        endpoint = `${this.apiUrl}/register/customer`;
        payload.address = data.address;
        payload.phoneNumber = data.phoneNumber;
        break;
      case 'AGENT':
        endpoint = `${this.apiUrl}/register/agent`;
        payload.licenseNumber = data.licenseNumber;
        break;
      case 'EMPLOYEE':
        endpoint = `${this.apiUrl}/register/employee`;
        payload.department = data.department;
        payload.designation = data.designation;
        break;
      default:
        throw new Error('Invalid role selected');
    }

    return this.http.post(endpoint, payload);
  }

  getRoleName(): string | null {
    const token = localStorage.getItem('accessToken');
    if (token) 
    {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.role || null;
    }
    return null;
  }

  // getAdminName(): string | null {
  //   const token = localStorage.getItem('accessToken');
  //   if (token) {
  //     const payload = JSON.parse(atob(token.split('.')[1]));
  //     return payload.name || null;
  //   }
  //   return null;
  // }

}