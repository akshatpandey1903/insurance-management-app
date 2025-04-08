import { Component, OnInit } from '@angular/core';
import { CityResponseDto, StateResponseDto } from '../../model';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-settings',
  standalone: false,
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent implements OnInit {
  states: StateResponseDto[] = [];
  cities: CityResponseDto[] = [];
  newState = { stateName: '' };
  newCity = { stateId: 0, cityName: '' };
  message: string = '';

  constructor(private adminService:AdminService){
   // this.loadStates();
  }
ngOnInit() {
    this.loadStates();}
 

  loadStates(){
    this.adminService.getAllStates().subscribe(
      {
        next:(data)=>{
          this.states=data;
          this.loadCitiesForStates();
        },
        error: (err) => this.message = 'Error loading states: ' + err.message
      }
    )
  }

  // loadCitiesForStates() {
  //   this.states.forEach(state =>{
  //     this.adminService.getCitiesByStateId(state.stateId).subscribe(
  //       {
  //         next: (cities) => this.cities = [...this.cities, ...cities],
  //         error: (err) => this.message = 'Error loading cities: ' + err.message
  //       }
  //     )
  //   })
  // }

  loadCitiesForStates() {
    this.cities = [];
    
    this.states.forEach(state => {
      this.adminService.getCitiesByStateId(state.stateId).subscribe({
        next: (cities) => {
          this.cities = [...this.cities, ...cities];
        },
        error: (err) => this.message = 'Error loading cities: ' + err.message
      });
    });
  }

  getCitiesByState(stateId: number): CityResponseDto[] {
    return this.cities.filter(city => city.stateId === stateId);
  }

  addState() {
    this.adminService.createState(this.newState).subscribe({
      next: (response) => {
        this.message = 'State added successfully';
        this.newState.stateName = '';
        this.loadStates();
      },
      error: (err) => this.message = 'Error adding state: ' + err.message
    });
  }

  deleteState(id: number) {
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

  addCity() {
    this.adminService.createCity(this.newCity).subscribe({
      next: (response) => {
        this.message = 'City added successfully';
        this.newCity = { stateId: 0, cityName: '' };
        alert('City added successfully');
        this.loadCitiesForStates();
      },
      error: (err) => this.message = 'Error adding city: ' + err.message
    });
  }

  deleteCity(id: number) {
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

}
