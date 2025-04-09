import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-add-customer',
  standalone: false,
  templateUrl: './add-customer.component.html',
  styleUrl: './add-customer.component.css'
})
export class AddCustomerComponent {

  customerForm: FormGroup;
  message: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, public router: Router) {
    this.customerForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      phoneNumber: ['', [Validators.required]],
      address: ['', [Validators.required]]
    });
  }

  ngOnInit() {}

  onSubmit() {
    if (this.customerForm.valid) {
      this.authService.register({ ...this.customerForm.value, role: 'CUSTOMER' }).subscribe({
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
