package com.aurionpro.app.service;

import org.springframework.web.multipart.MultipartFile;

import com.aurionpro.app.entity.DocumentType;

public interface CustomerDocumentService {
	String uploadDocument(int customerId, DocumentType documentType, MultipartFile file);
}
