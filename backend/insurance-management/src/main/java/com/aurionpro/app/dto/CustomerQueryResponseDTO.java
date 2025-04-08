package com.aurionpro.app.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerQueryResponseDTO {
    private int id;
    private String subject;
    private String message;
    private String status;
    private String adminResponse;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String customerName;
}

