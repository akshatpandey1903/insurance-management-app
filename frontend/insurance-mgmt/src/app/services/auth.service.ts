import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/app';

  constructor(private http: HttpClient , private router: Router) {}

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

    switch (data.role) {
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

  getUserId(): number {
    const userId = localStorage.getItem('userId');
    return userId ? Number(userId) : 0;
  }
  

  // loginAndRedirect(data: any, captchaResolved: boolean): Observable<any> {
  //   if (!data.username || !data.password || !captchaResolved) {
  //     console.log('Invalid login data or CAPTCHA not resolved');
  //     throw new Error('Invalid login data or CAPTCHA not resolved');
  //   }

  //   return this.http.post(`${this.apiUrl}/login`, data).pipe(
  //     tap((response: any) => {
  //       console.log('Login API response:', response);
  //       localStorage.setItem('userId', response.userId);
  //       localStorage.setItem('accessToken', response.accessToken);

        
  //       const role = response.role || this.getRoleName();
  //       this.redirectBasedOnRole(role);
  //     })
  //   );
  // }

  loginAndRedirect(data: any, captchaResolved: boolean): Observable<any> {
    if (!data.username || !data.password || !captchaResolved) {
      return throwError(() => new Error('Invalid login data or CAPTCHA not resolved'));
    }
  
    return this.http.post(`${this.apiUrl}/login`, data).pipe(
      tap((response: any) => {
        console.log('Login API response:', response);
        localStorage.setItem('userId', response.userId);
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('userRole', response.role);
  
        const role = response.role || this.getRoleName();
        this.redirectBasedOnRole(role);
      })
    );
  }
  

  private redirectBasedOnRole(roleName: string) {
    switch (roleName?.toUpperCase()) {
      case 'ADMIN':
        this.router.navigate(['/admin/dashboard']);
        break;
      case 'CUSTOMER':
        this.router.navigate(['/customer/dashboard']);
        break;
      case 'AGENT':
        this.router.navigate(['/agent/dashboard']);
        break;
      case 'EMPLOYEE':
        this.router.navigate(['/employee/dashboard']);
        break;
      default:
        console.error('Unknown role:', roleName);
        this.router.navigate(['/login']);
    }
  

}

getToken(): string {
  return localStorage.getItem('accessToken') || '';
}

isLoggedIn(): boolean {
  console.log(this.getToken());
  return !!this.getToken();
}


hasRole(role: string): boolean {
  console.log(this.getRoleName())
  return this.getRoleName() === role;
}

}