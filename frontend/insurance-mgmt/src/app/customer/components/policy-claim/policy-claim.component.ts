import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-policy-claim',
  standalone: false,
  templateUrl: './policy-claim.component.html',
  styleUrl: './policy-claim.component.css'
})
export class PolicyClaimComponent {
  claimForm!: FormGroup;
  claims: any[] = [];

  constructor(private fb: FormBuilder, private claimService: CustomerService , private authService : AuthService) {}

  ngOnInit(): void {
    this.claimForm = this.fb.group({
      policyId: ['', Validators.required],
      reason: ['', Validators.required]
    });

    this.loadClaims();
  }

  // submitClaim(): void {
  //   if (this.claimForm.valid) {
  //     this.claimService.submitClaim(this.claimForm.value , this.authService.getUserId()).subscribe({
  //       next: () => {
  //         alert('Claim submitted successfully');
  //         this.claimForm.reset();
  //         this.loadClaims();
  //       },
  //       error: err => alert('Error submitting claim')
  //     });
  //   }
  // }

  submitClaim(): void {
    if (this.claimForm.valid) {
      console.log("Submitting claim data:", this.claimForm.value);
      
      this.claimService.submitClaim(this.claimForm.value, this.authService.getUserId()).subscribe({
        next: () => {
          alert('Claim submitted successfully');
          this.claimForm.reset();
          this.loadClaims();
        },
        error: err => {
          console.error('Error submitting claim:', err);
          alert('Error submitting claim');
        }
      });
    }
  }
  

  loadClaims(): void {
    this.claimService.getMyClaims(this.authService.getUserId()).subscribe({
      next: data => this.claims = data,
      error: err => console.error(err)
    });
  }
}
