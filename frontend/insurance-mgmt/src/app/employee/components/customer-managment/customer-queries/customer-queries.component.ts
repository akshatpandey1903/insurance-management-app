import { Component, OnInit } from '@angular/core';
import { CustomerQueryResponseDTO } from '../../../../models/customer.model';
import { PageResponse } from '../../../../models/page.model';
import { QueryService } from '../../services/query.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-customer-queries',
  standalone: false,
  templateUrl: './customer-queries.component.html',
  styleUrls: ['./customer-queries.component.css']
})
export class CustomerQueriesComponent implements OnInit {
  queries: PageResponse<CustomerQueryResponseDTO> = {
    content: [],
    totalPages: 0,
    totalElements: 0,
    size: 0,
    isLast: true
  };

  currentPage = 0;
  pageSize = 10;
  selectedStatus: string = 'PENDING';
  statuses = ['ALL', 'PENDING', 'RESOLVED'];

  constructor(
    private queryService: QueryService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadQueries();
  }

  loadQueries(): void {
    const statusParam = this.selectedStatus === 'ALL' ? undefined : this.selectedStatus;

    this.queryService.getAllQueries(this.currentPage, this.pageSize, statusParam).subscribe({
      next: res => this.queries = res,
      error: () => this.toastr.error("Failed to load customer queries")
    });
  }

  respond(query: CustomerQueryResponseDTO): void {
    const responseText = prompt(`Respond to "${query.subject}":`);
    if (!responseText) return;

    this.queryService.respondToQuery(query.id, responseText).subscribe({
      next: () => {
        this.toastr.success("Response submitted successfully");
        this.loadQueries();
      },
      error: () => this.toastr.error("Failed to submit response")
    });
  }

  changePage(pageIndex: number): void {
    this.currentPage = pageIndex;
    this.loadQueries();
  }

  onStatusChange(): void {
    this.currentPage = 0;
    this.loadQueries();
  }
}
