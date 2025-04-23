import { Component, OnInit } from '@angular/core';
import { AgentCommissionReportDto } from '../../../../models/agent.model';
import { AgentReportService } from '../../services/agent-report.service';

@Component({
  selector: 'app-commission-report',
  standalone: false,
  templateUrl: './commission-report.component.html',
  styleUrls: ['./commission-report.component.css']
})
export class CommissionReportComponent implements OnInit {
  agentCommissions: AgentCommissionReportDto[] = [];
  page = 0;
  size = 10;
  totalPages = 0;
  keyword: string = '';

  constructor(private reportService: AgentReportService) {}

  searchAgents(): void {
    this.page = 0;
    this.fetchReport();
  }

  ngOnInit(): void {
    this.fetchReport();
  }

  fetchReport(): void {
    this.reportService.getAgentCommissionReport(this.page, this.size, this.keyword).subscribe(res => {
      this.agentCommissions = res.content;
      this.totalPages = res.totalPages;
    });
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.fetchReport();
    }
  }

  prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.fetchReport();
    }
  }
}
