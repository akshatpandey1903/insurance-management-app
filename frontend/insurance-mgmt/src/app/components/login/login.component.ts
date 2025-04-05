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

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    this.authService.login(this.loginData).subscribe(
      {
        next:response => {
          localStorage.setItem('accessToken', response.accessToken); // Store the access token in local storage
          console.log('Login successful', response.accessToken);
          this.router.navigate(['/dashboard']); // Redirect to dashboard after login
        },
        error:error => console.error('Login failed', error)
      }
    );
  }
}
