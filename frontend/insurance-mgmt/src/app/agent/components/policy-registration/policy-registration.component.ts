import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { InsurancePlanService } from '../../services/insuranc-plan.service';
import { CustomerService } from '../../services/customer.service';
import { PolicyService } from '../../services/policy.service';
import { ToastrService } from 'ngx-toastr';
import { CustomerPolicyRequestDTO } from '../../../models/customer.model';

@Component({
  selector: 'app-policy-registration',
  standalone: false,
  templateUrl: './policy-registration.component.html',
  styleUrls: ['./policy-registration.component.css']
})
export class PolicyRegistrationComponent implements OnInit {

  policyForm!: FormGroup;
  customers: any[] = [];
  plans: any[] = [];
  agentLicenseNumber: string | null = null;


  constructor(
    private fb: FormBuilder,
    private insurancePlanService: InsurancePlanService,
    private customerService: CustomerService,
    private policyService: PolicyService,
    private toastr: ToastrService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadCustomers();  // Load all customers
    this.loadInsurancePlans();  // Load all insurance plans
    this.initializeForm();
    this.agentLicenseNumber = localStorage.getItem('licenseNumber');
    console.log(this.agentLicenseNumber);
  }

  initializeForm() {
    this.policyForm = this.fb.group({
      customerId: [null, Validators.required],
      insurancePlanId: [null, Validators.required],
      selectedDurationYears: [null, [Validators.required, Validators.min(1)]],
      selectedCoverageAmount: [null, [Validators.required, Validators.min(1)]],
      paymentFrequency: ['MONTHLY', Validators.required],
      licenseNumber: this.agentLicenseNumber
    });
  }

  loadCustomers() {
    this.customerService.getAllCustomers().subscribe(
      (data: any[]) => {
        this.customers = data;
        console.log(this.customers);
      },
      (error) => {
        this.toastr.error('Failed to load customers');
      }
    );
  }

  loadInsurancePlans() {
    this.insurancePlanService.getAllPlans().subscribe(
      (data: any[]) => {
        this.plans = data;
      },
      (error) => {
        this.toastr.error('Failed to load insurance plans');
      }
    );
  }

  onSubmit() {
    if (this.policyForm.invalid || !this.policyForm.value.customerId) {
      this.toastr.error('Please complete the form and select a customer');
      return;
    }
  
    const requestBody: CustomerPolicyRequestDTO = {
      ...this.policyForm.value,
      licenseNumber: this.agentLicenseNumber // set license from localStorage
    };
  
    this.policyService.registerPolicy(requestBody, this.policyForm.value.customerId).subscribe({
      next: (res) => {
        this.toastr.success('Policy registered successfully');
        this.policyForm.reset();
      },
      error: (err) => {
        this.toastr.error('Failed to register policy');
        console.error(err);
      }
    });
  }
}
