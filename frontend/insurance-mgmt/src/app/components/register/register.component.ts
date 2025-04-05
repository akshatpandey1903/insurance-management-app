import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { first } from 'rxjs';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})

export class RegisterComponent {
  registerData = {
    username: '',
    firstName: '',
    lastName:'',
    email: '',
    password: '',
    role: '',
    address: '',
    phoneNumber: '',
    department: '',
    designation: '',
    licenseNumber: ''
  };

  errors: { [key: string]: string } = {};
  generalError: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  // onRegister() {
  //   this.authService.register(this.registerData).subscribe(
  //     {
  //       next:response => {
  //         console.log('Registration successful', response);
  //         localStorage.setItem('accessToken', response.accessToken);
  //         this.router.navigate(['/login']);
  //       },
  //       error:error => console.error('Registration failed', error)
  //     }
      
  //   );
  // }


  onRegister() {
    this.errors = {}; // Reset errors before submission
    this.generalError = '';

    this.authService.register(this.registerData).subscribe(
      (response) => {
        console.log('Registration successful', response);
        this.router.navigate(['/login']);
      },
      (error) => {
        console.error('Registration failed', error);
        if (error.error?.errors) {
          // Handle field-specific validation errors
          error.error.errors.forEach((err: { field: string; message: string }) => {
            this.errors[err.field] = err.message;
          });
        } 
        
        else {
          // Handle generic errors (e.g., "Email already exists")
          this.generalError = error.error?.message || 'An unexpected error occurred. Please try again.';
        }
      }
    );
  }

  // Helper to check if a field has an error
  hasError(field: string): boolean {
    return !!this.errors[field];
  }

}
