import { Component, OnInit } from '@angular/core';
import { CityResponseDto, InsurancePlanResponseDTO, InsuranceTypeResponseDTO, StateResponseDto } from '../../model';
import { AdminService } from '../../services/admin.service';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-settings',
  standalone: false,
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent implements OnInit {
  documentTypes: string[] = ['AADHAR', 'PANCARD', 'LICENSE', 'PASSPORT', 'BIRTHCERTIFICATE','BILL','VOTERID'];
  insuranceTypeForm: FormGroup;
  insurancePlanForm: FormGroup;
  insuranceTypes: InsuranceTypeResponseDTO[] = [];
  insurancePlans: InsurancePlanResponseDTO[] = [];
  message: string = '';
  selectedPlan: InsurancePlanResponseDTO | null = null;
  private modalInstance: any;

  constructor(private fb: FormBuilder, private adminService: AdminService) {
    this.insuranceTypeForm = this.fb.group({
      typeName: ['', Validators.required]
    });
    this.insurancePlanForm = this.fb.group({
      planName: ['', Validators.required],
      minCoverageAmount: ['',Validators.required],
      maxCoverageAmount: ['',Validators.required],
      insuranceTypeId: ['', Validators.required],
      premiumRatePerThousandPerYear: ['', Validators.required],
      minDurationYears: ['', Validators.required],
      maxDurationYears: ['', Validators.required],
      description: ['', Validators.required],
      commissionRate: ['', Validators.required],
      requiredDocuments: this.fb.array([], Validators.required)
    });    
  }

  ngOnInit() {
    const accordionItems = document.querySelectorAll('.accordion-collapse');
    accordionItems.forEach(item => {
      new (window as any).bootstrap.Collapse(item, {
        toggle: false
      });
    });
    this.loadInsuranceTypes();
  }

  openPlanDetailsModal(plan: InsurancePlanResponseDTO) {
    this.selectedPlan = plan;
    const modalElement = document.getElementById('planDetailsModal');
    if (modalElement) {
      this.modalInstance = new (window as any).bootstrap.Modal(modalElement, {
        backdrop: 'static', // Prevent closing on backdrop click (optional)
        keyboard: true // Allow closing with ESC key
      });
      this.modalInstance.show();
    } else {
      console.error('Modal element not found');
    }
  }

  // Optional: Method to programmatically close the modal
  closeModal() {
    if (this.modalInstance) {
      this.modalInstance.hide();
      this.selectedPlan = null; // Clear selected plan
    }
  }

  loadInsuranceTypes() {
    this.adminService.getAllInsuranceTypes().subscribe({
      next: (data) => {
        this.insuranceTypes = data;
        this.loadInsurancePlans();
      },
      error: (err) => this.message = 'Error loading insurance types: ' + err.message
    });
  }

  loadInsurancePlans() {
    this.insurancePlans = []; 
    this.adminService.getAllInsurancePlans().subscribe({
      next: response => {
        console.log("Loaded Plans:", response); // Add this line
        this.insurancePlans = response;
      },
      error: error => {
        alert("Error while loading plan");
      }
    });
  }

  getPlansById(typeIName: string): InsurancePlanResponseDTO[] {
    return this.insurancePlans.filter(plan => plan.insuranceTypeName === typeIName);
  }

  onAddInsuranceType() {
    if (this.insuranceTypeForm.valid) {
      this.adminService.createInsuranceType(this.insuranceTypeForm.value).subscribe({
        next: () => {
          alert("Insurance Type added successfully");
          this.message = 'Insurance type added successfully';
          this.insuranceTypeForm.reset();
          this.loadInsuranceTypes();
        },
        error: (err) => this.message = 'Error adding insurance type: ' + err.message
      });
    }
  }

  onDocumentCheckboxChange(event: any) {
    const selectedDocs = this.insurancePlanForm.get('requiredDocuments') as FormArray;
  
    if (event.target.checked) {
      selectedDocs.push(this.fb.control(event.target.value));
    } else {
      const index = selectedDocs.controls.findIndex(x => x.value === event.target.value);
      if (index !== -1) {
        selectedDocs.removeAt(index);
      }
    }
  }

  onDeleteInsuranceType(id: number) {
    if (confirm('Are you sure you want to delete this insurance type?')) {
      this.adminService.deleteInsuranceType(id).subscribe({
        next: () => {
          this.message = 'Insurance type deleted successfully';
          this.loadInsuranceTypes();
        },
        error: (err) => this.message = 'Error deleting insurance type: ' + err.message
      });
    }
  }

  // onAddInsurancePlan() {
  //   if (this.insurancePlanForm.valid) {
  //     this.adminService.createInsurancePlan(this.insurancePlanForm.value).subscribe({
  //       next: () => {
  //         alert("Insurance Plan added successfully");
  //         this.message = 'Insurance plan added successfully';
  //         this.insurancePlanForm.reset();
  //         this.loadInsurancePlans();
  //       },
  //       error: (err) => this.message = 'Error adding insurance plan: ' + err.message
  //     });
  //   }
  // }

  onAddInsurancePlan() {
    if (this.insurancePlanForm.valid) {
      console.log('Form Data:', this.insurancePlanForm.value); // Log form data
      this.adminService.createInsurancePlan(this.insurancePlanForm.value).subscribe({
        next: (response) => {
          console.log('Plan Added Response:', response); // Log success response
          alert("Insurance Plan added successfully");
          this.message = 'Insurance plan added successfully';
          this.insurancePlanForm.reset();
          this.loadInsurancePlans();
        },
        error: (err) => {
          console.error('Error Adding Plan:', err); // Log error
          this.message = 'Error adding insurance plan: ' + err.message;
          if (err.status === 400) {
            this.message += ' - Check form data or backend validation.';
          } else if (err.status === 401) {
            this.message += ' - Unauthorized, check token.';
          }
        }
      });
    } else {
      console.log('Form is invalid:', this.insurancePlanForm.errors); // Log validation errors
      this.message = 'Form is invalid. Please fill all required fields.';
    }
  }

  onDeleteInsurancePlan(id: number) {
    if (confirm('Are you sure you want to delete this insurance plan?')) {
      this.adminService.deleteInsurancePlan(id).subscribe({
        next: () => {
          this.message = 'Insurance plan deleted successfully';
          this.loadInsurancePlans();
        },
        error: (err) => this.message = 'Error deleting insurance plan: ' + err.message
      });
    }
  }

}
