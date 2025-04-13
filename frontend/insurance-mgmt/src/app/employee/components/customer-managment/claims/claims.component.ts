import { Component, OnInit } from '@angular/core';
import { ClaimService } from '../../services/claim.service';
import { PageResponse } from '../../../../models/page.model';
import { ToastrService } from 'ngx-toastr';
import { ClaimApprovalRequestDTO, PolicyClaimResponseDTO } from '../../../../models/claim.model';

@Component({
  selector: 'app-claims',
  standalone: false,
  templateUrl: './claims.component.html',
  styleUrl: './claims.component.css'
})
export class ClaimsComponent {
  claims: PageResponse<PolicyClaimResponseDTO> = {
    content: [],
    totalPages: 0,
    totalElements: 0,
    size: 0,
    isLast: true
  };

  currentPage = 0;
  pageSize = 10;

  constructor(
    private claimService: ClaimService,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadClaims();
  }

  loadClaims(): void {
    this.claimService.getPendingClaims(this.currentPage, this.pageSize).subscribe({
      next: res => this.claims = res,
      error: () => this.toast.error('Failed to load claims')
    });
  }

  approveOrReject(claimId: number, status: 'APPROVED' | 'REJECTED'): void {
    const remarks = prompt(`Enter remarks for ${status.toLowerCase()} claim:`);
    if (!remarks) return;

    const request: ClaimApprovalRequestDTO = {
      claimId,
      employeeId: Number(localStorage.getItem('userId')),
      status,
      remarks
    };

    this.claimService.approveOrRejectClaim(request).subscribe({
      next: () => {
        this.toast.success(`Claim ${status.toLowerCase()} successfully`);
        this.loadClaims();
      },
      error: () => this.toast.error(`Failed to ${status.toLowerCase()} claim`)
    });
  }
}
