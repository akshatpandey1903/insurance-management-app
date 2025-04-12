import { Component, OnInit } from '@angular/core';
import { LoginComponent } from '../../../components/login/login.component';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-admindashboard',
  standalone: false,
  templateUrl: './admindashboard.component.html',
  styleUrl: './admindashboard.component.css'
})
export class AdmindashboardComponent
{
  adminName: string = 'Admin';

  constructor(private authService: AuthService) {}
  

}
