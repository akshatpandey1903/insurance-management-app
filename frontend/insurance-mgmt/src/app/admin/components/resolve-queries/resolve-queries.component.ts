import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { CustomerQueryResponse } from '../../../customer/model';
import { CustomerQueryResponseDTO } from '../../../models/customer.model';
import { QueryService } from '../../../employee/components/services/query.service';
import { ToastrService } from 'ngx-toastr';
import { PageResponse } from '../../../models/page.model';

@Component({
  selector: 'app-resolve-queries',
  standalone: false,
  templateUrl: './resolve-queries.component.html',
  styleUrl: './resolve-queries.component.css'
})
export class ResolveQueriesComponent {
  // unresolvedQueries: CustomerQueryResponse[] = [];
  // errorMsg: string = '';
  // resolvedQueries: CustomerQueryResponse[] = [];


  // constructor(private adminService: AdminService) {}

  // ngOnInit(): void {
  //   this.fetchPendingQueries();
  // }

  // fetchPendingQueries(): void {
  //   this.adminService.getUnresolvedQueries().subscribe({
  //     next: (res) => {
  //       console.log('Raw response from backend:', res);
    
  //       const allQueries = res.content; // <- grab the actual array
    
  //       if (Array.isArray(allQueries)) {
  //         this.unresolvedQueries = allQueries.filter(
  //           query => query.status === 'PENDING'
  //         );
  //       } else {
  //         console.error('Expected content to be an array but got:', allQueries);
  //       }
  //     },
  //     error: (err) => {
  //       console.error('Error fetching queries:', err);
  //       this.errorMsg = 'Failed to fetch queries';
  //     }
  //   });
    
  // }

  // fetchPendingQueries(): void {
  //   this.adminService.getUnresolvedQueries().subscribe({
  //     next: (res) => {
  //       console.log('Raw response from backend:', res);
  
  //       const allQueries = res.content;
  
  //       if (Array.isArray(allQueries)) {
  //         this.unresolvedQueries = allQueries.filter(query => query.status === 'PENDING');
  //         this.resolvedQueries = allQueries.filter(query => query.status === 'RESOLVED');
  //       } else {
  //         console.error('Expected content to be an array but got:', allQueries);
  //       }
  //     },
  //     error: (err) => {
  //       console.error('Error fetching queries:', err);
  //       this.errorMsg = 'Failed to fetch queries';
  //     }
  //   });
  // }

  // resolve(queryId: number, response: string): void {
  //   this.adminService.resolveQuery(queryId, response).subscribe({
  //     next: () => {
  //       this.fetchPendingQueries(); // Refresh the list after resolution
  //     },
  //     error: (err) => {
  //       console.error(err);
  //       this.errorMsg = 'Failed to resolve the query';
  //     }
  //   });
  //  }

  queries: PageResponse<CustomerQueryResponseDTO> = {
    content: [],
    totalPages: 0,
    totalElements: 0,
    size: 0,
    isLast: true
  };

  currentPage = 0;
  pageSize = 5;
  selectedStatus: string = 'PENDING';
  statuses = ['ALL', 'PENDING', 'RESOLVED'];
  errorMsg: string = '';
  Math = Math;

  constructor(
    private adminService: AdminService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadQueries();
  }

  loadQueries(): void {
    const statusParam = this.selectedStatus === 'ALL' ? undefined : this.selectedStatus;

    this.adminService.getQueries(this.currentPage, this.pageSize, statusParam).subscribe({
      next: res => this.queries = res,
      error: (err) => {
        console.error('Error fetching queries:', err);
        this.toastr.error("Failed to load customer queries");
        this.errorMsg = 'Failed to fetch queries';
      }
    });
  }

  respond(query: CustomerQueryResponseDTO): void {
    const responseText = prompt(`Respond to "${query.subject}":`);
    if (!responseText) return;

    this.adminService.resolveQuery(query.id, responseText).subscribe({
      next: () => {
        this.toastr.success("Response submitted successfully");
        this.loadQueries();
      },
      error: () => {
        this.toastr.error("Failed to submit response");
        this.errorMsg = 'Failed to resolve the query';
      }
    });
  }

  // changePage(pageIndex: number): void {
  //   this.currentPage = pageIndex;
  //   this.loadQueries();
  // }

  // onStatusChange(): void {
  //   this.currentPage = 0;
  //   this.loadQueries();
  // }

  changePage(pageIndex: number): void {
    if (pageIndex < 0 || (this.queries.totalPages > 0 && pageIndex >= this.queries.totalPages)) {
      return;
    }
    
    this.currentPage = pageIndex;
    this.loadQueries();
  }

  onStatusChange(): void {
    this.currentPage = 0;
    this.loadQueries();
  }
  
  onPageSizeChange(): void {
    this.currentPage = 0;
    this.loadQueries();
  }
  
  // Returns the page numbers that should be visible in pagination
  getVisiblePages(): number[] {
    const totalPages = this.queries.totalPages;
    if (totalPages <= 7) {
      // If 7 or fewer pages, show all
      return Array.from({ length: totalPages }, (_, i) => i);
    }
    
    // Always include current page, 2 before and 2 after when possible
    const pages: number[] = [];
    
    // Always include first page
    pages.push(0);
    
    // Current page and surrounding pages
    const startPage = Math.max(1, this.currentPage - 2);
    const endPage = Math.min(totalPages - 2, this.currentPage + 2);
    
    // Add ellipsis after first page if needed
    if (startPage > 1) {
      pages.push(-1); // -1 represents ellipsis
    }
    
    // Add visible page numbers
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    // Add ellipsis before last page if needed
    if (endPage < totalPages - 2) {
      pages.push(-2); // -2 represents ellipsis
    }
    
    // Always include last page
    pages.push(totalPages - 1);
    
    return pages;
  }
}
