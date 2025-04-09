package com.aurionpro.app.service;

import org.springframework.web.multipart.MultipartFile;

import com.aurionpro.app.dto.CustomerDocumentResponseDTO;
import com.aurionpro.app.dto.DocumentStatusUpdateRequestDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.DocumentType;

public interface CustomerDocumentService {
	String uploadDocument(int customerId, DocumentType documentType, MultipartFile file);
	PageResponse<CustomerDocumentResponseDTO> getPendingDocuments(int pageNumber, int pageSize);
	void updateDocumentStatus(DocumentStatusUpdateRequestDTO request, int employeeId);
}
