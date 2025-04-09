import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-add-employee',
  standalone: false,
  templateUrl: './add-employee.component.html',
  styleUrl: './add-employee.component.css'
})
export class AddEmployeeComponent {
  employeeForm: FormGroup;
  message: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, public router: Router) {
    this.employeeForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      phone: ['', [Validators.required]],
      department: ['', [Validators.required]],
      designation: ['', [Validators.required]]
    });
  }

  ngOnInit() {}

  onSubmit() {
    if (this.employeeForm.valid) {
      this.authService.register({ ...this.employeeForm.value, role: 'EMPLOYEE' }).subscribe({
        next: response => {
          console.log('Registration successful', response);
          this.router.navigate(['/login']);
        },
        error: error => {
          console.error('Registration failed', error);
        }
      });
      
    }
  }

}
