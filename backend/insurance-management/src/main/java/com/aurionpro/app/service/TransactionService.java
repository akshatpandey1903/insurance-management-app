package com.aurionpro.app.service;

import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.TransactionRequestDTO;
import com.aurionpro.app.dto.TransactionResponseDTO;
import com.aurionpro.app.entity.CustomerPolicy;
import com.aurionpro.app.entity.TransactionType;
import com.aurionpro.app.entity.WithdrawalRequest;

public interface TransactionService {
	void recordPolicyPurchaseTransaction(CustomerPolicy policy);
	void recordWithdrawalTransaction(WithdrawalRequest withdrawalRequest);
	PageResponse<TransactionResponseDTO> getAllTransactions(int pageNumber, int pageSize);
	PageResponse<TransactionResponseDTO> getTransactionsByType(TransactionType type, int pageNumber, int pageSize);
	TransactionResponseDTO createCustomerPremiumTransaction(TransactionRequestDTO requestDTO, int customerId);
}
