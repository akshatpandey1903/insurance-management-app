import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AgentService } from '../../services/agent.service';

@Component({
  selector: 'app-agent-registration',
  standalone: false,
  templateUrl: './agent-registration.component.html',
  styleUrl: './agent-registration.component.css',
})
export class AgentRegistrationComponent {
  agentForm: FormGroup;
  toastMessage = '';
  isSubmitted = false;

  constructor(private fb: FormBuilder, private agentService: AgentService) {
    this.agentForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(5)]],
      password: [
        '',
        [
          Validators.required,
          Validators.pattern(
            '^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\d\\s:])([^\\s]){8,16}$'
          ),
        ],
      ],
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      licenseNumber: ['', Validators.required],
    });
  }

  onSubmit(): void {
    this.isSubmitted = true;

    if (this.agentForm.invalid) return;

    this.agentService.registerAgent(this.agentForm.value).subscribe({
      next: () => {
        this.toastMessage = 'Agent registered successfully!';
        this.agentForm.reset();
        this.isSubmitted = false;
      },
      error: (err: any) => {
        this.toastMessage = err?.error?.message || 'Registration failed.';
      },
    });
  }

  closeToast(): void {
    this.toastMessage = '';
  }
}
