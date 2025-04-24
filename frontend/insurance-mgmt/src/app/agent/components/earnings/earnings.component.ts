import { Component } from '@angular/core';
import { AgentService } from '../../services/agent.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

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

  downloadPdf(): void {
    const doc = new jsPDF();
    const currencySymbol = "Rs.";

    doc.setFontSize(16);
    doc.text("Earnings Report", 14, 15);

    doc.setFontSize(12);
    doc.text(`Name: ${this.commissionReport.firstName} ${this.commissionReport.lastName}`, 14, 25);
    doc.text(`Email: ${this.commissionReport.email}`, 14, 32);
    doc.text(`Total Policies Sold: ${this.commissionReport.totalPoliciesSold}`, 14, 39);
    doc.text(`Average Commission Rate: ${this.commissionReport.totalCommissionRate}%`, 14, 46);
    doc.text(`Total Earnings: ${currencySymbol}${this.commissionReport.totalEarnings}`, 14, 53);

    // Set column widths to prevent overflow
    const columns = [
      { header: 'Customer', dataKey: 'customer' },
      { header: 'Policy', dataKey: 'policy' },
      { header: 'Premium', dataKey: 'premium' },
      { header: 'Commission %', dataKey: 'commissionRate' },
      { header: 'Earnings', dataKey: 'earnings' },
      { header: 'Start Date', dataKey: 'startDate' }
    ];

    // Prepare data rows
    const rows = this.earningsDetails.map(e => ({
      customer: e.customerName,
      policy: e.insurancePlanName,
      premium: `${currencySymbol}${e.premiumPaid}`,
      commissionRate: `${e.commissionRate}%`,
      earnings: `${currencySymbol}${e.commissionEarned}`,
      startDate: e.startDate
    }));

    autoTable(doc, {
      startY: 60,
      columns: columns,
      body: rows,
      columnStyles: {
        customer: { cellWidth: 30 },
        policy: { cellWidth: 30 },
        premium: { cellWidth: 25 },
        commissionRate: { cellWidth: 25 },
        earnings: { cellWidth: 25 },
        startDate: { cellWidth: 25 }
      },
      margin: { left: 14 }
    });
    
    const date = new Date();
    const currentDate = date.toLocaleDateString();
    doc.save(`earnings-report-${currentDate}.pdf`);
  }
}