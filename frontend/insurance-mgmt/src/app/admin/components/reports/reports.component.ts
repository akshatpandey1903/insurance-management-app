import { Component } from '@angular/core';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-reports',
  standalone: false,
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent {

  reports = [
    { title: 'Customer Report', icon: 'bi-person-lines-fill', path: '/admin/reports/customers', color: 'primary' },
    { title: 'Agent Report', icon: 'bi-person-badge', path: '/admin/reports/agents', color: 'info' },
    { title: 'Commission Report', icon: 'bi-currency-rupee', path: '/admin/reports/commissions', color: 'success' },
    { title: 'Policy Payments', icon: 'bi-file-earmark-text', path: '/admin/reports/policy-payments', color: 'warning' },
    { title: 'Transactions', icon: 'bi-bar-chart-line', path: '/admin/reports/transactions', color: 'primary' }
  ];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    // Optional: Load summary stats
  }
}
