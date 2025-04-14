import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AgentProfileDTO } from '../../model';
import { AgentService } from '../../services/agent.service';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  profile!: AgentProfileDTO;
  agentId!: number;

  constructor(private agentService: AgentService) {}

  ngOnInit(): void {
    this.agentId = this.getLoggedInAgentId();
    this.loadProfile();
  }

  getLoggedInAgentId(): number {
    // ⚠️ Update this if needed based on your JWT/local storage logic
    return Number(localStorage.getItem('userId'));
  }

  loadProfile(): void {
    this.agentService.getProfile(this.agentId).subscribe({
      next: (data) => {
        this.profile = data;
      },
      error: (err) => {
        console.error('Error loading profile:', err);
      }
    });
  }
}
