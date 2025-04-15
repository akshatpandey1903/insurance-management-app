import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { AuthService } from '../../../services/auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CustomerDocumentResponseDTO, CustomerKaPolicyResponseDTO } from '../../model';
import { AdminService } from '../../../admin/services/admin.service';
import { InsurancePlanResponseDTO } from '../../../admin/model';

@Component({
  selector: 'app-policies',
  standalone: false,
  templateUrl: './policies.component.html',
  styleUrl: './policies.component.css'
})
export class PoliciesComponent {
  registerForm: FormGroup;
  frequencies = ['MONTHLY', 'QUARTERLY', 'HALF_YEARLY', 'YEARLY'];
  response: any;
  error: string = '';
  uploadForm: FormGroup;
  documentTypes = ['AADHAR', 'PANCARD', 'LICENSE', 'VOTERID','PASSPORT','BILL','BIRTHCERTIFICATE'];
  selectedFile!: File;
  selectedType!: string;
  message = '';
  customerId: number;
  purchasedPolicies: CustomerKaPolicyResponseDTO[] = [];
  availablePlans: InsurancePlanResponseDTO[] = [];
  filteredDocumentTypes: string[] = [];
  documentList: CustomerDocumentResponseDTO[] = [];

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private authService: AuthService,
    private adminService: AdminService,
    private http: HttpClient
  ) {
    this.registerForm = this.fb.group({
      insurancePlanId: [null, Validators.required],
      paymentFrequency: ['', Validators.required],
      selectedCoverageAmount: ['', Validators.required],
      selectedDurationYears: ['', Validators.required],
      licenseNumber: [null]
    });

    this.uploadForm = this.fb.group({
      documentType: [''],
      file: [null]
    });
    this.customerId = this.authService.getUserId();
  }

  ngOnInit(): void {
    this.registerForm.get('insurancePlanId')?.valueChanges.subscribe(planId => {
      const selectedPlan = this.availablePlans.find(p => p.insurancePlanId === planId);
      this.filteredDocumentTypes = selectedPlan?.requiredDocuments || [];
    });
    this.loadPolicies();
    this.loadPlans();
    this.loadCustomerDocuments(); 
  }

  loadPolicies() {
    const customerId = this.authService.getUserId(); 
    this.customerService.getPurchasedPolicies(customerId).subscribe({
      next: (res) => {
        this.purchasedPolicies = res;
        console.log(this.purchasedPolicies)
      },
      error: (err) => {
        console.error('Failed to fetch policies', err);
      }
    });
  }

  loadPlans() {
    this.adminService.getAllInsurancePlans().subscribe({
      next: (res) => {
        this.availablePlans = res;
      },
      error: (err) => {
        console.error('Failed to load plans', err);
      }
    });
  }

  loadCustomerDocuments(): void {
    this.customerService.getCustomerDocuments(this.customerId).subscribe({
      next: (docs) => {
        this.documentList = docs;
      },
      error: (err) => {
        console.error('Failed to load customer documents:', err);
      }
    });
  }

  onSubmit() {
    if (this.registerForm.invalid) return;

    const customerId = this.authService.getUserId(); // Assuming from token or storage
    const payload = this.registerForm.value;

    this.customerService.registerPolicy(customerId, payload).subscribe({
      next: (res) => {
        this.response = res;
        this.error = '';
        this.registerForm.reset();
      },
      error: (err) => {
        this.error = err?.error?.message || 'Something went wrong';
        this.response = null;
      }
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadDocument() {
    if (!this.selectedFile || !this.uploadForm.value.documentType) return;

    const formData = new FormData();
    formData.append('customerId', this.customerId.toString());
    formData.append('documentType', this.uploadForm.value.documentType);
    formData.append('file', this.selectedFile);

    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.authService.getToken()}`);
    console.log(this.authService.getToken()); 
    this.http.post(`http://localhost:8080/app/documents/upload`, formData, { headers, responseType: 'text' })
      .subscribe({
        next: (res) => {
          this.message = 'Document uploaded successfully!';
          this.uploadForm.reset();
        },
        error: (err) => {
          this.message = 'Upload failed';
          console.error(err);
        }
      });
  }
}
