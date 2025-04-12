import { Component } from '@angular/core';
import { AdminService } from '../../../services/admin.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-agent-report',
  standalone: false,
  templateUrl: './agent-report.component.html',
  styleUrl: './agent-report.component.css'
})
export class AgentReportComponent {
  agents: any[] = [];
  pageNumber: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadAgents();
  }

  loadAgents(): void {
    this.adminService.getAllAgents(this.pageNumber, this.pageSize).subscribe({
      next: (res) => {
        this.agents = res.content;
        this.totalPages = res.totalPages;
      },
      error: (err) => {
        console.error('Error loading agents', err);
      }
    });
  }

  nextPage(): void {
    if (this.pageNumber < this.totalPages - 1) {
      this.pageNumber++;
      this.loadAgents();
    }
  }

  prevPage(): void {
    if (this.pageNumber > 0) {
      this.pageNumber--;
      this.loadAgents();
    }
  }

  downloadPdf(): void {
    const doc = new jsPDF();
    doc.setFontSize(14);
    doc.text('Agent Report', 14, 14);

    const headers = [['#', 'Name', 'Email', 'Approved By', 'Policies', 'Commission', 'Registered At']];
    const data = this.agents.map((agent, i) => [
      i + 1,
      agent.name,
      agent.email,
      agent.approvedBy,
      agent.totalPoliciesRegistered,
      agent.totalCommissionEarnedYearly,
      new Date(agent.registeredAt).toLocaleDateString()
    ]);

    autoTable(doc, {
      head: headers,
      body: data,
      startY: 20
    });

    doc.save('agent-report.pdf');
  }
}
