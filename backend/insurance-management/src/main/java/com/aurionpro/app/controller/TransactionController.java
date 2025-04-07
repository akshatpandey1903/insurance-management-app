package com.aurionpro.app.controller;

import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.TransactionResponseDTO;
import com.aurionpro.app.entity.TransactionType;
import com.aurionpro.app.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public PageResponse<TransactionResponseDTO> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transactionService.getAllTransactions(page, size);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/type/{type}")
    public PageResponse<TransactionResponseDTO> getTransactionsByType(
            @PathVariable TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transactionService.getTransactionsByType(type, page, size);
    }
}
