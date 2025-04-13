import { Component, OnInit } from '@angular/core';
import { CustomerPolicyResponseDTO } from '../../../../models/customer.model';
import { PageResponse } from '../../../../models/page.model';
import { PolicyService } from '../../services/policy.service';
import { ToastrService } from 'ngx-toastr';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';

@Component({
  selector: 'app-policy-approval',
  standalone: false,
  templateUrl: './policy-approval.component.html',
  styleUrls: ['./policy-approval.component.css']
})
export class PolicyApprovalComponent implements OnInit {
  policies: PageResponse<CustomerPolicyResponseDTO> = {
    content: [],
    totalPages: 0,
    totalElements: 0,
    size: 0,
    isLast: true
  };
  currentPage = 1;
  pageSize = 10;

  constructor(
    private policyService: PolicyService,
    private toastService: ToastrService
  ) {}

  ngOnInit(): void {
    this.fetchPolicies();
  }

  fetchPolicies(): void {
    const apiPage = this.currentPage - 1;
    this.policyService.getUnapprovedPolicies(apiPage, this.pageSize).subscribe({
      next: (res) => {
        this.policies = res;
        console.log(res);
      },
      error: () => this.toastService.error('Failed to fetch policies')
    });
  }

  onPageChange(event: PageChangedEvent): void {
    this.currentPage = event.page - 1;
    this.fetchPolicies();
  }

  approvePolicy(policyId: number): void {
    const employeeId = Number(localStorage.getItem('userId'));
    if (confirm("Approve this policy?")) {
      this.policyService.approvePolicy(policyId).subscribe({
        next: () => {
          this.toastService.success("Policy approved successfully");
          this.fetchPolicies();
        },
        error: () => this.toastService.error("Failed to approve policy")
      });
    }
  }

  rejectPolicy(policyId: number): void {
    const reason = prompt("Enter rejection reason:");
    if (!reason?.trim()) {
      this.toastService.warning("Rejection reason is required");
      return;
    }
  
    this.policyService.rejectPolicy(policyId, reason).subscribe({
      next: () => {
        this.toastService.success("Policy rejected successfully");
        this.fetchPolicies();
      },
      error: () => this.toastService.error("Failed to reject policy")
    });
  }
  
}
