import { Component, OnInit } from '@angular/core';
import { AgentReportService } from '../../services/agent-report.service';
import { ToastrService } from 'ngx-toastr';
import { AgentReportResponseDTO } from '../../../../models/agent.model';
import { PageResponse } from '../../../../models/page.model';

@Component({
  selector: 'app-all-agents',
  standalone: false,
  templateUrl: './all-agents.component.html',
  styleUrl: './all-agents.component.css'
})
export class AllAgentsComponent {
  agents: PageResponse<AgentReportResponseDTO> = {
    content: [],
    totalPages: 0,
    totalElements: 0,
    size: 0,
    isLast: true
  };

  currentPage = 0;
  pageSize = 10;

  constructor(private agentReportService: AgentReportService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.loadAgents();
  }

  loadAgents(): void {
    this.agentReportService.getAgentReports(this.currentPage, this.pageSize).subscribe({
      next: res => this.agents = res,
      error: () => this.toastr.error("Failed to load agent data")
    });
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadAgents();
  }
}
