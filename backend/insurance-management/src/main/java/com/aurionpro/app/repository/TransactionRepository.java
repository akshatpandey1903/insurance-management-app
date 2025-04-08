package com.aurionpro.app.repository;

import com.aurionpro.app.entity.Transaction;
import com.aurionpro.app.entity.TransactionType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	Page<Transaction> findByTransactionTypeAndIsDeletedFalse(TransactionType type, Pageable pageable);
	boolean existsByCustomerPolicyId(int policyId);
}