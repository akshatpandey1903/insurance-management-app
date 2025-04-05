import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'insurance-mgmt';

  constructor(private router: Router) {}

  // Getter for checking authentication status
 isAuthenticated() {
    // return !!localStorage.getItem('authToken'); // Check if authToken exists
  }
}
