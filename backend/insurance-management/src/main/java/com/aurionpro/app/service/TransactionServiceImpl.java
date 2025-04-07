package com.aurionpro.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.TransactionResponseDTO;
import com.aurionpro.app.entity.CustomerPolicy;
import com.aurionpro.app.entity.Transaction;
import com.aurionpro.app.entity.TransactionType;
import com.aurionpro.app.entity.WithdrawalRequest;
import com.aurionpro.app.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
    private TransactionRepository transactionRepository;

    public void recordPolicyPurchaseTransaction(CustomerPolicy policy) {
        Transaction transaction = new Transaction();
        transaction.setAmount(policy.getCalculatedPremium());
        transaction.setDescription("Policy Purchase - " + policy.getInsurancePlan().getPlanName());
        transaction.setTransactionType(TransactionType.POLICY_PURCHASE);
        transaction.setUser(policy.getCustomer());
        transaction.setUserRole("CUSTOMER");
        transaction.setCustomerPolicy(policy);

        transactionRepository.save(transaction);
    }
    
    public void recordWithdrawalTransaction(WithdrawalRequest withdrawalRequest) {
        Transaction transaction = new Transaction();
        transaction.setAmount(withdrawalRequest.getAmount());
        transaction.setDescription("Withdrawal Approved");
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setUser(withdrawalRequest.getRequestedBy());
        transaction.setUserRole(withdrawalRequest.getUserRole());
        transaction.setWithdrawalRequest(withdrawalRequest);

        transactionRepository.save(transaction);
    }
    
    public PageResponse<TransactionResponseDTO> getAllTransactions(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("transactionTime").descending());
        Page<Transaction> transactions = transactionRepository.findAll(pageable);

        List<TransactionResponseDTO> content = transactions.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());

        return new PageResponse<>(
        		content,
        		transactions.getTotalPages(),
        		transactions.getTotalElements(),
        		transactions.getSize(),
        		transactions.isLast()
            );
    }

    public PageResponse<TransactionResponseDTO> getTransactionsByType(TransactionType type, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("transactionTime").descending());
        Page<Transaction> transactions = transactionRepository.findByTransactionType(type, pageable);

        List<TransactionResponseDTO> content = transactions.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());

        return new PageResponse<>(
        		content,
        		transactions.getTotalPages(),
        		transactions.getTotalElements(),
        		transactions.getSize(),
        		transactions.isLast()
            );
    }

    private TransactionResponseDTO mapToDTO(Transaction t) {
        return new TransactionResponseDTO(
            t.getTransactionId(),
            t.getAmount(),
            t.getDescription(),
            t.getTransactionType(),
            t.getTransactionTime(),
            t.getUser().getFirstName() + " " + t.getUser().getLastName(),
            t.getUserRole()
        );
    }
}
