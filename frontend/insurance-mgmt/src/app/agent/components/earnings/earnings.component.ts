import { Component } from '@angular/core';
import { AgentService } from '../../services/agent.service';

@Component({
  selector: 'app-earnings',
  standalone: false,
  templateUrl: './earnings.component.html',
  styleUrl: './earnings.component.css'
})
export class EarningsComponent {
  agentId: number = Number(localStorage.getItem('userId'));
  commissionReport: any;
  earningsDetails: any[] = [];

  constructor(private agentService: AgentService) {}

  ngOnInit(): void {
    this.loadCommissionReport();
    this.loadEarningsDetails();
  }

  loadCommissionReport(): void {
    this.agentService.getCommissionReport(this.agentId).subscribe({
      next: data => this.commissionReport = data,
      error: err => console.error(err)
    });
  }

  loadEarningsDetails(): void {
    this.agentService.getEarningsDetails(this.agentId).subscribe({
      next: data => this.earningsDetails = data,
      error: err => console.error(err)
    });
  }
}
