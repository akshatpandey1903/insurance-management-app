import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-agent-navbar',
  standalone: false,
  templateUrl: './agent-navbar.component.html',
  styleUrl: './agent-navbar.component.css'
})
export class AgentNavbarComponent {
  constructor(private router: Router){}

  logout() {
    localStorage.removeItem('accessToken');
    this.router.navigate(['/login']);
  }
}
