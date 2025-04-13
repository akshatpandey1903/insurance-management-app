import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { DocumentService } from '../../services/document.service';
import { PageResponse } from '../../../../models/page.model';
import { CustomerDocumentResponseDTO, DocumentStatusUpdateRequestDTO } from '../../../../models/document.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';

@Component({
  selector: 'app-document-verification',
  standalone: false,
  templateUrl: './document-verification.component.html',
  styleUrls: ['./document-verification.component.css']
})
export class DocumentVerificationComponent implements OnInit {
  pendingDocuments: PageResponse<CustomerDocumentResponseDTO> = {
    content: [],
    totalPages: 0,
    totalElements: 0,
    size: 0,
    isLast: true
  };
  currentPage = 1; // UI is 1-based
  pageSize = 10;
  selectedDoc: CustomerDocumentResponseDTO | null = null;
  @ViewChild('previewModal') previewModal!: TemplateRef<any>;

  constructor(
    private documentService: DocumentService,
    private modalService: NgbModal,
    private toastService: ToastrService
  ) {}

  ngOnInit(): void {
    this.getPendingDocuments();
  }

  getPendingDocuments(): void {
    // Convert to zero-based for the API
    const apiPage = this.currentPage - 1;
    
    this.documentService.getPendingDocuments(apiPage, this.pageSize).subscribe({
      next: (res) => {
        this.pendingDocuments = res;
        console.log('Pending documents:', res);
      },
      error: (err) => {
        console.error('Error fetching documents:', err);
        this.toastService.error('Failed to fetch pending documents');
      }
    });
  } 

  onPageChange(event: PageChangedEvent): void {
    this.currentPage = event.page; // Keep as 1-based for UI
    this.getPendingDocuments();
  }

  openPreview(doc: CustomerDocumentResponseDTO): void {
    this.selectedDoc = doc;
    this.modalService.open(this.previewModal, { size: 'lg', centered: true });
  }

  confirmAction(documentId: number, status: 'APPROVED' | 'REJECTED'): void {
    if (confirm(`Are you sure you want to ${status.toLowerCase()} this document?`)) {
      const employeeId = Number(localStorage.getItem('userId'));
      const request: DocumentStatusUpdateRequestDTO = { documentId, status };
      this.documentService.approveOrRejectDocument(request, employeeId).subscribe({
        next: () => {
          this.toastService.success(`Document ${status.toLowerCase()} successfully`);
          this.getPendingDocuments();
        },
        error: () => this.toastService.error(`Failed to ${status.toLowerCase()} document`)
      });
    }
  }
}