package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aurionpro.app.dto.CustomerDocumentResponseDTO;
import com.aurionpro.app.dto.DocumentApprovalRequestDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.DocumentType;
import com.aurionpro.app.service.CustomerDocumentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app/documents")
@RequiredArgsConstructor
public class CustomerDocumentController {
	
	@Autowired
	private final CustomerDocumentService documentService;
	
	@PostMapping("/upload")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<String> uploadDocument(
			@RequestParam("customerId") int customerId,
            @RequestParam("documentType") DocumentType documentType,
            @RequestParam("file") MultipartFile file){
		String documentUrl = documentService.uploadDocument(customerId, documentType, file);
        return ResponseEntity.ok(documentUrl);
	}
	
	@GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
    public PageResponse<CustomerDocumentResponseDTO> getPendingDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return documentService.getPendingDocuments(page, size);
    }
	
	@PostMapping("/approve/{employeeId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYEE')")
	public ResponseEntity<String> approveDocument(@RequestBody DocumentApprovalRequestDTO request, @PathVariable int employeeId) {
		documentService.approveDocument(request, employeeId);
	    return ResponseEntity.ok("Document approved successfully.");
	}

}
