import { Component, OnInit } from '@angular/core';
import { CustomerReportService } from '../../services/customer-report.service';
import { ToastrService } from 'ngx-toastr';
import { CustomerReportDto } from '../../../../models/customer.model';
import { PageResponse } from '../../../../models/page.model';

@Component({
  selector: 'app-customer-list',
  standalone: false,
  templateUrl: './customer-list.component.html',
  styleUrl: './customer-list.component.css'
})
export class CustomerListComponent {
  customers: PageResponse<CustomerReportDto> = {
    content: [],
    totalPages: 0,
    totalElements: 0,
    size: 0,
    isLast: true
  };

  currentPage = 0;
  pageSize = 10;

  constructor(private customerReportService: CustomerReportService, private toastr: ToastrService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerReportService.getCustomerReports(this.currentPage, this.pageSize).subscribe({
      next: res => this.customers = res,
      error: () => this.toastr.error("Failed to load customer data")
    });
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.loadCustomers();
  }
}
