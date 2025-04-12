import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';
import { CustomerQueryResponse } from '../../../customer/model';

@Component({
  selector: 'app-resolve-queries',
  standalone: false,
  templateUrl: './resolve-queries.component.html',
  styleUrl: './resolve-queries.component.css'
})
export class ResolveQueriesComponent {
  unresolvedQueries: CustomerQueryResponse[] = [];
  errorMsg: string = '';

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.fetchPendingQueries();
  }

  fetchPendingQueries(): void {
    this.adminService.getUnresolvedQueries().subscribe({
      next: (res) => {
        console.log('Raw response from backend:', res);
    
        const allQueries = res.content; // <- grab the actual array
    
        if (Array.isArray(allQueries)) {
          this.unresolvedQueries = allQueries.filter(
            query => query.status === 'PENDING'
          );
        } else {
          console.error('Expected content to be an array but got:', allQueries);
        }
      },
      error: (err) => {
        console.error('Error fetching queries:', err);
        this.errorMsg = 'Failed to fetch queries';
      }
    });
    
  }

  resolve(queryId: number, response: string): void {
    this.adminService.resolveQuery(queryId, response).subscribe({
      next: () => {
        this.fetchPendingQueries(); // Refresh the list after resolution
      },
      error: (err) => {
        console.error(err);
        this.errorMsg = 'Failed to resolve the query';
      }
    });
  }
}
