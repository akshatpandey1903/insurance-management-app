export type DocumentStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

export type DocumentType = 'AADHAR' | 'PANCARD' | 'DRIVING_LICENSE' | 'PASSPORT'; // Add all types you support

export interface DocumentStatusUpdateRequestDTO {
  documentId: number;
  status: DocumentStatus;
  rejectionReason?: string;
}

export interface CustomerDocumentResponseDTO {
  documentId: number;
  customerId: number;
  customerName: string;
  documentType: DocumentType;
  documentUrl: string;
  documentStatus: DocumentStatus;
  uploadedAt: Date; // or Date if you want to convert
}
