import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { first } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})

export class RegisterComponent {
  // registerData = {
  //   username: '',
  //   firstName: '',
  //   lastName:'',
  //   email: '',
  //   password: '',
  //   role: '',
  //   address: '',
  //   phoneNumber: '',
  //   department: '',
  //   designation: '',
  //   licenseNumber: ''
  // };

  // errors: { [key: string]: string } = {};
  // generalError: string = '';

  // constructor(private authService: AuthService, private router: Router) {}

  // onRegister() {
  //   this.errors = {}; // Reset errors before submission
  //   this.generalError = '';

  //   this.authService.register(this.registerData).subscribe(
  //     {
  //       next :response => {
  //         console.log('Registration successful', response);
  //         this.router.navigate(['/login']);
  //       },
  //       error: error => {
  //         console.error('Registration failed', error);
  //         if (error.error?.errors) {
  //           // Handle field-specific validation errors
  //           error.error.errors.forEach((err: { field: string; message: string }) => {
  //             this.errors[err.field] = err.message;
  //           });
  //         } 
          
  //         else {
  //           // Handle generic errors (e.g., "Email already exists")
  //           this.generalError = error.error?.message || 'An unexpected error occurred. Please try again.';
  //         }
  //       }
  //     }
  //   );
  // }

  // // Helper to check if a field has an error
  // hasError(field: string): boolean {
  //   return !!this.errors[field];
  // }

  registerForm: FormGroup;
  errors: { [key: string]: string } = {};
  generalError: string = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(5)]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.pattern(
            /^(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\w\d\s:]).{8,16}$/
          ),
        ],
      ],
      role: ['', Validators.required],
      address: [''],
      phoneNumber: ['', [Validators.minLength(10), Validators.maxLength(15)]],
      department: [''],
      designation: [''],
      licenseNumber: [''],
    });

    // Add conditional validators based on role
    this.registerForm.get('role')?.valueChanges.subscribe((role) => {
      this.updateValidators(role);
    });
  }

  updateValidators(role: string) {
    const address = this.registerForm.get('address');
    const phoneNumber = this.registerForm.get('phoneNumber');
    const department = this.registerForm.get('department');
    const designation = this.registerForm.get('designation');
    const licenseNumber = this.registerForm.get('licenseNumber');

    // Reset validators
    address?.clearValidators();
    phoneNumber?.clearValidators();
    department?.clearValidators();
    designation?.clearValidators();
    licenseNumber?.clearValidators();

    if (role === 'CUSTOMER') {
      address?.setValidators([Validators.required]);
      phoneNumber?.setValidators([
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(15),
      ]);
    } else if (role === 'EMPLOYEE') {
      department?.setValidators([Validators.required]);
      designation?.setValidators([Validators.required]);
    } else if (role === 'AGENT') {
      licenseNumber?.setValidators([Validators.required]);
    }

    // Update validator status
    address?.updateValueAndValidity();
    phoneNumber?.updateValueAndValidity();
    department?.updateValueAndValidity();
    designation?.updateValueAndValidity();
    licenseNumber?.updateValueAndValidity();
  }

  onRegister() {
    this.errors = {};
    this.generalError = '';

    if (this.registerForm.invalid) {
      this.markFormGroupTouched(this.registerForm);
      this.setFormErrors();
      return;
    }

    this.authService.register(this.registerForm.value).subscribe({
      next: (response) => {
        console.log('Registration successful', response);
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Registration failed', error);
        if (error.error?.errors) {
          error.error.errors.forEach((err: { field: string; message: string }) => {
            this.errors[err.field] = err.message;
          });
        } else {
          this.generalError =
            error.error?.message || 'An unexpected error occurred. Please try again.';
        }
      },
    });
  }

  // Helper to mark all fields as touched
  markFormGroupTouched(formGroup: FormGroup) {
    Object.values(formGroup.controls).forEach((control) => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  // Set form errors for display
  setFormErrors() {
    const controls = this.registerForm.controls;
    for (const name in controls) {
      if (controls[name].invalid) {
        if (controls[name].errors?.['required']) {
          this.errors[name] = `${name.charAt(0).toUpperCase() + name.slice(1)} is required`;
        } else if (controls[name].errors?.['minlength']) {
          this.errors[name] = `${name.charAt(0).toUpperCase() + name.slice(1)} must be at least ${
            controls[name].errors?.['minlength'].requiredLength
          } characters`;
        } else if (controls[name].errors?.['maxlength']) {
          this.errors[name] = `${name.charAt(0).toUpperCase() + name.slice(1)} must not exceed ${
            controls[name].errors?.['maxlength'].requiredLength
          } characters`;
        } else if (controls[name].errors?.['email']) {
          this.errors[name] = 'Invalid email format';
        } else if (controls[name].errors?.['pattern']) {
          this.errors[name] =
            name === 'password'
              ? 'Password must be 8-16 characters, include a digit, uppercase, lowercase, and special character'
              : 'Invalid format';
        }
      }
    }
  }

  // Helper to check if a field has an error
  hasError(field: string): boolean {
    const control = this.registerForm.get(field);
    return !!this.errors[field] || (control?.invalid && control?.touched) || false;
  }

  // Get error message for a field
  getError(field: string): string {
    return this.errors[field] || '';
  }

}
