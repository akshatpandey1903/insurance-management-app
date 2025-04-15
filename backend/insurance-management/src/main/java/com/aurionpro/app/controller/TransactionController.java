package com.aurionpro.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.TransactionRequestDTO;
import com.aurionpro.app.dto.TransactionResponseDTO;
import com.aurionpro.app.entity.TransactionType;
import com.aurionpro.app.service.TransactionService;

@RestController
@RequestMapping("/app/transactions")
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
    
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/make-transaction")
    public ResponseEntity<TransactionResponseDTO> makePremiumPayment(
            @RequestBody TransactionRequestDTO requestDto,
            @RequestParam int customerId) {
        
        TransactionResponseDTO response = transactionService.createCustomerPremiumTransaction(requestDto, customerId);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/premium/transactions/user/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByUserId(@PathVariable int userId, Pageable pageable) {
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByUserId(userId, pageable);
        return ResponseEntity.ok(transactions);
    }

}
