import { Component , ViewChild , Inject, PLATFORM_ID } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  isBrowser: boolean; 
  loginForm: FormGroup;
  loginData = { username: '', password: '' };
  email: string = '';
  captchaResolved: boolean = false;
  siteKey: string = "6Lc07w0rAAAAADkK9dwLh0JoZoUI7u5aJzz3ou6A";
  errorMessage: string = '';

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {

    this.isBrowser = isPlatformBrowser(this.platformId);

    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
      // recaptcha: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  // onLogin() {
  //   if (!this.loginForm.valid || !this.captchaResolved) {
  //     console.log('Please complete the form and CAPTCHA');
  //     return;
  //   }
  
  //   const loginData = this.loginForm.value;
  
  //   this.authService.loginAndRedirect(loginData, this.captchaResolved).subscribe({
  //     next: () => console.log('Login & Redirect Successful'),
  //     error: err => console.error('Login failed', err)
  //   });
  // }

  onLogin(): void {
    if (this.loginForm.invalid || !this.captchaResolved) return;
  
    const loginData = this.loginForm.value;
  
    this.authService.loginAndRedirect(loginData, this.captchaResolved).subscribe({
      error: (err) => {
        console.error('Login error:', err);
        if (err.status === 401 || err.status === 403 || err.status === 404) {
          this.errorMessage = 'Invalid username or password.';
        } else {
          this.errorMessage = err.message || 'Something went wrong. Please try again.';
        }
      }
    });
  }

  getLoggedInUser() {
    return JSON.parse(localStorage.getItem('user')!);
  }

  getUserId(): number {
    const token = localStorage.getItem('accessToken');
    if (!token) return 0;
  
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.userId || 0;
    } catch (e) {
      console.error('Invalid token:', e);
      return 0;
    }
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

  onCaptchaResolved(event: any) {
    console.log('CAPTCHA event:', event); // Debug the event structure
    const captchaResponse = event.response || event || '';
    this.captchaResolved = !!captchaResponse;
    console.log('CAPTCHA resolved with response:', captchaResponse);
  }
}
