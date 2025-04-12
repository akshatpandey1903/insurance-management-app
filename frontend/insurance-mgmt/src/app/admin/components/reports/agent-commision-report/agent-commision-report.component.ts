import { Component } from '@angular/core';
import { AgentCommissionReportDto } from '../../../model';
import { AdminService } from '../../../services/admin.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-agent-commision-report',
  standalone: false,
  templateUrl: './agent-commision-report.component.html',
  styleUrl: './agent-commision-report.component.css'
})
export class AgentCommisionReportComponent {
  agents: AgentCommissionReportDto[] = [];
  page = 0;
  size = 10;
  totalPages = 0;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.fetchAgentCommissions();
  }

  fetchAgentCommissions(): void {
    this.adminService.getAgentCommissionReport(this.page, this.size).subscribe({
      next: (res) => {
        this.agents = res.content;
        this.totalPages = res.totalPages;
      },
      error: (err) => console.error('Error fetching report', err)
    });
  }

  goToPage(pageNumber: number): void {
    if (pageNumber >= 0 && pageNumber < this.totalPages) {
      this.page = pageNumber;
      this.fetchAgentCommissions();
    }
  }

  downloadPdf(): void {
    const doc = new jsPDF();
    doc.setFontSize(16);
    doc.text('Agent Commission Report', 14, 20);
    autoTable(doc, {
      startY: 30,
      head: [[
        'Agent ID', 'Name', 'Email',
        'Total Policies Sold', 'Commission Rate (%)', 'Total Earnings (₹)'
      ]],
      body: this.agents.map(agent => [
        agent.userId,
        `${agent.firstName} ${agent.lastName}`,
        agent.email,
        agent.totalPoliciesSold,
        agent.totalCommissionRate,
        agent.totalEarnings
      ])
    });
    doc.save('agent-commission-report.pdf');
  }
}
