import { Component } from '@angular/core';
import { CustomerAndPolicyDTO, InsurancePlan } from '../../model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AgentService } from '../../services/agent.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-policy-registration',
  standalone: false,
  templateUrl: './policy-registration.component.html',
  styleUrl: './policy-registration.component.css'
})
export class PolicyRegistrationComponent {
 
}
