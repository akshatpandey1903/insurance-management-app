import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../../services/auth.service';
import { CustomerService } from '../../services/customer.service';
import { CustomerKaPolicyResponseDTO } from '../../model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-policy-cancel',
  standalone: false,
  templateUrl: './policy-cancel.component.html',
  styleUrl: './policy-cancel.component.css'
})

export class PolicyCancelComponent {
  // customerId!: number;
  purchasedPolicies: CustomerKaPolicyResponseDTO[] = [];
  cancelPolicyForm!: FormGroup;

  constructor(
    private customerService: CustomerService,
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private authService: AuthService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      // this.customerId = +params.get('id')!;
      this.loadPolicies();
    });

    this.cancelPolicyForm = this.fb.group({
      policyId: ['', Validators.required],
    });
  }

  loadPolicies() {
    const customerId = this.authService.getUserId();
    this.customerService.getAllPolicies(customerId).subscribe({
      next: (res) => {
        console.log('Policies received:', res); // ✅ Add this
        this.purchasedPolicies = res;
      },
      error: (err) => {
        console.error('Failed to fetch policies', err);
      }
    });
  }
  

  onCancelPolicy(): void {
    if (this.cancelPolicyForm.invalid) {
      this.toastr.warning('Please select a policy to cancel');
      return;
    }

    const policyId = this.cancelPolicyForm.value.policyId;
    if (policyId) {
      this.customerService.cancelPolicy(this.authService.getUserId(), policyId).subscribe({
        next: (response) => {
          this.toastr.success('Policy cancelled successfully!');
          this.loadPolicies();
        },
        error: (error) => {
          console.error('Error canceling policy:', error);
          this.toastr.error('Failed to cancel the policy');
        }
      });
    }
  }
}
