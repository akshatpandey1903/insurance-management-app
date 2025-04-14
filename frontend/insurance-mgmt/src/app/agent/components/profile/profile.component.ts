import { Component } from '@angular/core';
import { AgentUpdateRequest } from '../../../models/agent.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AgentService } from '../../services/agent.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  profileForm!: FormGroup;
  agentId: number = Number(localStorage.getItem('userId'));

  constructor(
    private fb: FormBuilder,
    private agentService: AgentService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.profileForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['']
    });

    this.agentService.getAgentProfile(this.agentId).subscribe({
      next: (data) => {
        const [firstName, lastName] = data.fullName.split(' ');
        this.profileForm.patchValue({
          firstName,
          lastName,
          username: data.username,
          email: data.email
        });
      },
      error: () => {
        this.toastr.error('Failed to load profile');
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid) return;

    const updateData: AgentUpdateRequest = this.profileForm.value;

    this.agentService.updateAgentProfile(this.agentId, updateData).subscribe({
      next: () => this.toastr.success('Profile updated successfully'),
      error: () => this.toastr.error('Failed to update profile')
    });
  }
}
