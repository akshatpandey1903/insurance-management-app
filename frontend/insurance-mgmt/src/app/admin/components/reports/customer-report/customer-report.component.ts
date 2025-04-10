import { Component } from '@angular/core';
import { AdminService } from '../../../services/admin.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-customer-report',
  standalone: false,
  templateUrl: './customer-report.component.html',
  styleUrl: './customer-report.component.css'
})
export class CustomerReportComponent {
  customers: any[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 3;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.fetchCustomers();
  }

  fetchCustomers(): void {
    this.adminService.getCustomerReports(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.customers = response.content;
        this.totalPages = response.totalPages;
      },
      error: (err) => {
        console.error('Error fetching customers', err);
      }
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.fetchCustomers();
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
  
    // Title
    doc.setFontSize(18);
    doc.text('Customer Report', 14, 20);
  
    // Generation Date
    const currentDate = new Date().toLocaleDateString();
    doc.setFontSize(11);
    doc.setTextColor(100);
    doc.text(`Generated on: ${currentDate}`, 14, 28);
  
    // Table Content
    const tableData = this.customers.map((customer, index) => [
      index + 1,
      `${customer.firstName} ${customer.lastName}`,
      customer.email,
      customer.phoneNumber,
      customer.totalPolicies
    ]);
  
    autoTable(doc, {
      startY: 35,
      head: [['#', 'Full Name', 'Email', 'Phone', 'Total Policies']],
      body: tableData,
      styles: { fontSize: 10 },
      headStyles: { fillColor: [22, 160, 133] }, // Teal header
      footStyles: { fillColor: [22, 160, 133] },
      alternateRowStyles: { fillColor: [240, 240, 240] },
      theme: 'striped'
    });
  
    // Save the file
    doc.save(`Customer_Report_${currentDate}.pdf`);
  }

  
}
