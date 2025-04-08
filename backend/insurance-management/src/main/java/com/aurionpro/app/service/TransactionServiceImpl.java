package com.aurionpro.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.TransactionRequestDTO;
import com.aurionpro.app.dto.TransactionResponseDTO;
import com.aurionpro.app.entity.CustomerPolicy;
import com.aurionpro.app.entity.Transaction;
import com.aurionpro.app.entity.TransactionType;
import com.aurionpro.app.entity.WithdrawalRequest;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.CustomerPolicyRepository;
import com.aurionpro.app.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
    private TransactionRepository transactionRepository;
	
	@Autowired
	private CustomerPolicyRepository customerPolicyRepository;

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
        Page<Transaction> transactions = transactionRepository.findByTransactionTypeAndIsDeletedFalse(type, pageable);

        List<TransactionResponseDTO> content = transactions.getContent().stream().map(this::mapToDTO).collect(Collectors.toList());

        return new PageResponse<>(
        		content,
        		transactions.getTotalPages(),
        		transactions.getTotalElements(),
        		transactions.getSize(),
        		transactions.isLast()
            );
    }
    
    @Override
    @Transactional
    public TransactionResponseDTO createCustomerPremiumTransaction(TransactionRequestDTO requestDTO, int customerId) {
        CustomerPolicy policy = customerPolicyRepository.findById(requestDTO.getCustomerPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Customer policy not found"));

        if (!policy.isActive()) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Policy is not active");
        }

        if (policy.getCustomer().getUserId() != customerId) {
            throw new ResourceNotFoundException(HttpStatus.FORBIDDEN, "This policy does not belong to the customer");
        }
        
        boolean isFirstTransaction = !transactionRepository
                .existsByCustomerPolicyId(policy.getId());
        
        LocalDate today = LocalDate.now();
        LocalDate nextDue = policy.getNextDueDate();
        
        // Only check 5-day window for non-first transactions
        if (!isFirstTransaction && today.isBefore(nextDue.minusDays(5))) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "You can only pay within 5 days of due date");
        }
        
        BigDecimal amount = policy.getCalculatedPremium();
        requestDTO.setAmount(amount);;
        
        // Create transaction
        Transaction txn = new Transaction();
        txn.setAmount(amount);
        txn.setDescription("Premium payment");
        txn.setTransactionType(TransactionType.PREMIUM_PAYMENT);
        txn.setUser(policy.getCustomer());
        txn.setUserRole("CUSTOMER");
        txn.setCustomerPolicy(policy);

        Transaction savedTxn = transactionRepository.save(txn);
       

        if (!isFirstTransaction) {
            // Not the first transaction — update nextDueDate
            LocalDate newDueDate = switch (policy.getPaymentFrequency()) {
                case MONTHLY -> nextDue.plusMonths(1);
                case QUARTERLY -> nextDue.plusMonths(3);
                case HALF_YEARLY -> nextDue.plusMonths(6);
                case YEARLY -> nextDue.plusYears(1);
            };
            policy.setNextDueDate(newDueDate);
            customerPolicyRepository.save(policy);
        }
        
        return mapToDTO(savedTxn);
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
