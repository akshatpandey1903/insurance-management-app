import { Component, OnInit } from '@angular/core';
import { AgentService } from '../../services/agent.service';
import { AgentResponseDTO } from '../../../../models/agent.model';
import { PageResponse } from '../../../../models/page.model';

@Component({
  selector: 'app-pending-agent-approval',
  templateUrl: './pending-agent-approval.component.html',
  standalone: false,
  styleUrls: ['./pending-agent-approval.component.css']
})
export class PendingAgentApprovalComponent implements OnInit {
  pendingAgents: PageResponse<AgentResponseDTO> = {
    content: [],
    totalPages: 0,
    totalElements: 0,
    size: 0,
    isLast: true,
  };  
  currentPage: number = 0;
  pageSize: number = 10;

  constructor(private agentService: AgentService) {}

  ngOnInit(): void {
    this.getPendingAgents();
  }

  // Get the list of pending agents
  getPendingAgents(): void {
    this.agentService.getPendingAgents(this.currentPage, this.pageSize)
      .subscribe(response => {
        this.pendingAgents = response;
      });
  }

  // Approve an agent
  approveAgent(agentId: number): void {
    const confirmed = window.confirm('Are you sure you want to approve this agent?');
    if (!confirmed) return;
    const approverId = 1;  // Replace with the logged-in employee ID
    this.agentService.approveAgent(agentId).subscribe(() => {
      this.getPendingAgents();  // Refresh the list
    });
  }

  // Reject an agent
  rejectAgent(agentId: number): void {
    const confirmed = window.confirm('Are you sure you want to reject this agent?');
    if (!confirmed) return;
    const approverId = 1;  // Replace with the logged-in employee ID
    this.agentService.rejectAgent(agentId).subscribe(() => {
      this.getPendingAgents();  // Refresh the list
    });
  }

// Add this method to your component class
getPagesArray(): number[] {
  const pages: number[] = [];
  for (let i = 1; i <= this.pendingAgents.totalPages; i++) {
    pages.push(i);
  }
  return pages;
}

// Update your onPageChange method to handle edge cases
onPageChange(page: number): void {
  if (page <= 0) {
    this.currentPage = 0;
  } else if (page > this.pendingAgents.totalPages) {
    this.currentPage = this.pendingAgents.totalPages - 1;
  } else {
    this.currentPage = page - 1;  // Convert to 0-based index
  }
  this.getPendingAgents();
}
}
