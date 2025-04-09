package com.aurionpro.app.dto;

import com.aurionpro.app.entity.DocumentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentStatusUpdateRequestDTO {
	private int documentId;
	private DocumentStatus status;
	private String rejectionReason;
}
 