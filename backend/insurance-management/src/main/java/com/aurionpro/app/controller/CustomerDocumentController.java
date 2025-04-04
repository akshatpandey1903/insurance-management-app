package com.aurionpro.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	public ResponseEntity<String> uploadDocument(
			@RequestParam("customerId") int customerId,
            @RequestParam("documentType") DocumentType documentType,
            @RequestParam("file") MultipartFile file){
		String documentUrl = documentService.uploadDocument(customerId, documentType, file);
        return ResponseEntity.ok(documentUrl);
	}
}
