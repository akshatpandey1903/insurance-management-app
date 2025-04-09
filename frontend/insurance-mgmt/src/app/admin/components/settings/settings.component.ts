import { Component, OnInit } from '@angular/core';
import { CityResponseDto, InsurancePlanResponseDTO, InsuranceTypeResponseDTO, StateResponseDto } from '../../model';
import { AdminService } from '../../services/admin.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-settings',
  standalone: false,
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent implements OnInit {
  stateForm: FormGroup;
  cityForm: FormGroup;
  insuranceTypeForm: FormGroup;
  insurancePlanForm: FormGroup;
  states: StateResponseDto[] = [];
  cities: CityResponseDto[] = [];
  insuranceTypes: InsuranceTypeResponseDTO[] = [];
  insurancePlans: InsurancePlanResponseDTO[] = [];
  message: string = '';

  constructor(private fb: FormBuilder, private adminService: AdminService) {
    this.stateForm = this.fb.group({
      stateName: ['', Validators.required]
    });
    this.cityForm = this.fb.group({
      cityName: ['', Validators.required],
      stateId: ['', Validators.required]
    });
    this.insuranceTypeForm = this.fb.group({
      typeName: ['', Validators.required]
    });
    this.insurancePlanForm = this.fb.group({
      planName: ['', Validators.required],
      insuranceTypeId: ['', Validators.required],
      yearlyPremiumAmount: ['', Validators.required],
      coverageAmount: ['', Validators.required],
      durationYears: ['', Validators.required],
      description: ['', Validators.required],
      commissionRate: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.loadStates();
    this.loadInsuranceTypes();
  }

  loadStates() {
    this.adminService.getAllStates().subscribe({
      next: (data) => {
        this.states = data;
        this.loadCitiesForStates();
      },
      error: (err) => this.message = 'Error loading states: ' + err.message
    });
  }

  // loadCitiesForStates() {
  //   this.cities = [];
  
  //   this.states.forEach(state =>{
  //     this.adminService.getCitiesByStateId(state.stateId).subscribe({
  //       next: (citiesForState) => {
  //         console.log('Cities for state', state.stateId, citiesForState); // Check what you get
  //         if (citiesForState) {
  //           this.cities = [...this.cities, ...citiesForState];
  //         }
  //       },
  //       error: (err) => {
  //         console.error('Error loading cities for state', state.stateId, err);
  //       }
  //     });
  //   });
  // }

  loadCitiesForStates() {
    this.cities = []; // Clear existing cities
  
    this.states.forEach(state => {
      this.adminService.getCitiesByStateId(state.stateId).subscribe(citiesForState => {
        if (citiesForState) {
          this.cities = [...this.cities, ...citiesForState]; // Merge cities
        }
      });
    });
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

  getCitiesByState(stateId: number): CityResponseDto[] {
    return this.cities.filter(city => city.stateId === stateId);
  }

  getPlansById(typeIName: string): InsurancePlanResponseDTO[] {
    return this.insurancePlans.filter(plan => plan.insuranceTypeName === typeIName);
  }

  onAddState() {
    if (this.stateForm.valid) {
      this.adminService.createState(this.stateForm.value).subscribe({
        next: () => {
          this.message = 'State added successfully';
          this.stateForm.reset();
          this.loadStates();
        },
        error: (err) => this.message = 'Error adding state: ' + err.message
      });
    }
  }

  onDeleteState(id: number) {
    if (confirm('Are you sure you want to delete this state?')) {
      this.adminService.deleteState(id).subscribe({
        next: () => {
          this.message = 'State deleted successfully';
          this.loadStates();
        },
        error: (err) => this.message = 'Error deleting state: ' + err.message
      });
    }
  }

  onAddCity() {
    if (this.cityForm.valid) {
      this.adminService.createCity(this.cityForm.value).subscribe({
        next: () => {
          this.message = 'City added successfully';
          this.cityForm.reset();
          this.loadCitiesForStates();
        },
        error: (err) => this.message = 'Error adding city: ' + err.message
      });
    }
  }

  onDeleteCity(id: number) {
    if (confirm('Are you sure you want to delete this city?')) {
      this.adminService.deleteCity(id).subscribe({
        next: () => {
          this.message = 'City deleted successfully';
          this.loadCitiesForStates();
        },
        error: (err) => this.message = 'Error deleting city: ' + err.message
      });
    }
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

  onAddInsurancePlan() {
    if (this.insurancePlanForm.valid) {
      this.adminService.createInsurancePlan(this.insurancePlanForm.value).subscribe({
        next: () => {
          alert("Insurance Type added successfully");
          this.message = 'Insurance plan added successfully';
          this.insurancePlanForm.reset();
          this.loadInsurancePlans();
        },
        error: (err) => this.message = 'Error adding insurance plan: ' + err.message
      });
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
