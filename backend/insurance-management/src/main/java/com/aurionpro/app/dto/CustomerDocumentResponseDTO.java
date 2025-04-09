package com.aurionpro.app.dto;

import java.time.LocalDateTime;

import com.aurionpro.app.entity.DocumentType;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDocumentResponseDTO {
    private int documentId;
    private int customerId; 
    private String customerName;
    private DocumentType documentType;
    private String documentUrl;
    private boolean isVerified;
    private LocalDateTime uploadedAt;
}

