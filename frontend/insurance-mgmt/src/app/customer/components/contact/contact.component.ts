import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from 'express';
import { CustomerService } from '../../services/customer.service';
import { AuthService } from '../../../services/auth.service';
import { CustomerQueryRequest, CustomerQueryResponse } from '../../model';
import { LoginComponent } from '../../../components/login/login.component';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-contact',
  standalone: false,
  templateUrl: './contact.component.html',
  styleUrl: './contact.component.css'
})
export class ContactComponent {
  contactForm!: FormGroup;
  successMsg = '';
  errorMsg = '';
  fetchError = '';
  queryHistory: CustomerQueryResponse[] = [];
  loading = false;
  userId: number = 0;

  constructor(private fb: FormBuilder, private customerService: CustomerService) {}

  ngOnInit(): void {
    this.contactForm = this.fb.group({
      subject: ['', Validators.required],
      message: ['', Validators.required]
    });

    this.userId = this.getUserIdFromToken();
    this.loadQueryHistory();
  }

  getUserIdFromToken(): number {
      const userId = localStorage.getItem('userId');
      return userId ? Number(userId) : 0;
  }

  onSubmit(): void {
    if (this.contactForm.invalid) return;

    const queryData = this.contactForm.value;
    this.customerService.raiseQuery(queryData, this.userId).subscribe({
      next: () => {
        this.successMsg = 'Query submitted successfully!';
        this.errorMsg = '';
        this.contactForm.reset();
        this.loadQueryHistory();
      },
      error: (err) => {
        this.errorMsg = 'Failed to submit query.';
        this.successMsg = '';
        console.error(err);
      }
    });
  }

  loadQueryHistory(): void {
    this.loading = true;
    this.customerService.getUserQueries(this.userId).subscribe({
      next: (data) => {
        this.queryHistory = data;
        this.loading = false;
      },
      error: (err) => {
        this.fetchError = 'Failed to fetch query history.';
        this.loading = false;
        console.error(err);
      }
    });
  }
 
}
