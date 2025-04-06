import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  loginData = { username: '', password: '' };
  email: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() 
  {
    this.authService.login(this.loginData).subscribe(
      {
        next:response => {
          console.log('Login successful', response);
          localStorage.setItem('user', JSON.stringify(response.user));
          localStorage.setItem('accessToken', response.accessToken); 
          console.log('Login successful', response.accessToken);
          const role = response.role || this.authService.getRoleName();
          this.redirectBasedOnRole(role);
        },
        error:error => console.error('Login failed', error)
      }
    );
  }

  getLoggedInUser() {
    return JSON.parse(localStorage.getItem('user')!);
  }

  private redirectBasedOnRole(roleName: string){
    switch (roleName.toUpperCase()) {
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

  openForgotPassword() {
    this.router.navigate(['/forgot-password']); // Navigate to forgot password page
  }
}
