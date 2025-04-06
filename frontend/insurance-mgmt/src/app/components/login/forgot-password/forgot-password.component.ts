import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  standalone: false,
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent implements OnInit {
  email:string='';
  token:string='';
  newPassword:string='';
  message:string='';
  isResetMode: boolean = false;

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    // Check if token is present in URL query params
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    if (this.token) {
      this.isResetMode = true; // Switch to reset mode if token is present
    }
  }

  sendResetLink() {
    this.http.post('http://localhost:8080/app/password/forgot-password', { email: this.email }, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          console.log('Response:', response);
          this.message = 'Password reset link sent to your email. Check your inbox.';
        },
        error: (error) => {
          console.error('Error:', error);
          this.message = 'Error sending reset link. Please try again.';
        }
      });
  }

  resetPassword() {
    if (!this.token) {
      this.message = 'Invalid or missing token. Please use the link from your email.';
      return;
    }
    this.http.post('http://localhost:8080/app/password/reset-password', {
      token: this.token,
      newPassword: this.newPassword
    }, { responseType: 'text' }).subscribe({
      next: (response) => {
        console.log('Response:', response);
        this.message = 'Password reset successfully. Redirecting to login...';
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (error) => {
        console.error('Error:', error);
        this.message = 'Error resetting password. Please try again.';
      }
    });
  }

}
