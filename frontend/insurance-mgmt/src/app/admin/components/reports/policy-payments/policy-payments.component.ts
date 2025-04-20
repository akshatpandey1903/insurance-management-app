import { Component } from '@angular/core';
import { PlanPurchaseReportDto } from '../../../model';
import { AdminService } from '../../../services/admin.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-policy-payments',
  standalone: false,
  templateUrl: './policy-payments.component.html',
  styleUrl: './policy-payments.component.css'
})
export class PolicyPaymentsComponent {
  policies: PlanPurchaseReportDto[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 5;
  isLoading: boolean = false;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.fetchPolicyPayments();
  }

  fetchPolicyPayments(): void {
    this.adminService.getPolicyPayments(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.policies = response.content;
        this.totalPages = response.totalPages;
      },
      error: (err) => {
        console.error('Error fetching policy payments', err);
      }
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.fetchPolicyPayments();
    }
  }

  nextPage(): void {
    this.goToPage(this.currentPage + 1);
  }

  prevPage(): void {
    this.goToPage(this.currentPage - 1);
  }

  downloadPDF(): void {
    const doc = new jsPDF();

    doc.setFontSize(18);
    doc.text('Policy Payment Report', 14, 20);

    const currentDate = new Date().toLocaleDateString();
    doc.setFontSize(11);
    doc.setTextColor(100);
    doc.text(`Generated on: ${currentDate}`, 14, 28);

    const tableData = this.policies.map((policy, index) => [
      index + 1,
      policy.customerName,
      policy.planName,
      policy.insuranceType,
      policy.calculatedPremium,
      policy.paymentFrequency,
      policy.agentName,
      policy.status
    ]);

    autoTable(doc, {
      startY: 35,
      head: [['#', 'Customer', 'Plan', 'Type', 'Premium', 'Frequency', 'Agent', 'Status']],
      body: tableData,
      styles: { fontSize: 10 },
      headStyles: { fillColor: [41, 128, 185] }, // Blue header
      alternateRowStyles: { fillColor: [245, 245, 245] },
      theme: 'striped'
    });

    doc.save(`Policy_Payments_${currentDate}.pdf`);
  }

}
