import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-agent',
  standalone: false,
  templateUrl: './add-agent.component.html',
  styleUrl: './add-agent.component.css'
})
export class AddAgentComponent {
  agentForm: FormGroup;
  message: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, public router: Router) {
    this.agentForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      licenseNumber: ['', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.agentForm.valid) {
      this.authService.register({ ...this.agentForm.value, role: 'AGENT' }).subscribe({
        next: response => {
          console.log('Registration successful', response);
          this.router.navigate(['/login']);
        },
        error: error => {
          console.error('Registration failed', error);
        }
      });
      
    }
  }
}
