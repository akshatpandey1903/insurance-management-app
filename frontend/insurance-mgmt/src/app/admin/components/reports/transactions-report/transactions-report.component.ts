import { Component } from '@angular/core';
import { TransactionResponse } from '../../../model';
import { AdminService } from '../../../services/admin.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

@Component({
  selector: 'app-transactions-report',
  standalone: false,
  templateUrl: './transactions-report.component.html',
  styleUrl: './transactions-report.component.css'
})
export class TransactionsReportComponent {
  transactions: TransactionResponse[] = [];
  currentPage: number = 0;
  totalPages: number = 0;
  pageSize: number = 5;
  sortField: string = '';
sortDirection: 'asc' | 'desc' = 'asc';

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.fetchTransactions();
  }

  fetchTransactions(): void {
    this.adminService.getTransactionReports(this.currentPage, this.pageSize).subscribe({
      next: (res) => {
        this.transactions = res.content;
        this.totalPages = res.totalPages;
      },
      error: (err) => console.error('Error fetching transactions:', err),
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.fetchTransactions();
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
    const currentDate = new Date().toLocaleDateString();

    doc.setFontSize(18);
    doc.text('Transaction Report', 14, 20);

    doc.setFontSize(11);
    doc.setTextColor(100);
    doc.text(`Generated on: ${currentDate}`, 14, 28);

    const tableData = this.transactions.map((txn, i) => [
      i + 1,
      txn.userFullName,
      txn.userRole,
      txn.transactionType,
      txn.amount,
      txn.description,
      new Date(txn.transactionTime).toLocaleString(),
    ]);

    autoTable(doc, {
      startY: 35,
      head: [['#', 'User', 'Role', 'Type', 'Amount', 'Description', 'Date']],
      body: tableData,
      styles: { fontSize: 9 },
      headStyles: { fillColor: [52, 73, 94] },
      alternateRowStyles: { fillColor: [245, 245, 245] },
      theme: 'striped',
    });

    doc.save(`Transaction_Report_${currentDate}.pdf`);
  }
}
