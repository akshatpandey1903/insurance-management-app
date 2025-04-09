package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.app.entity.CustomerDocument;

public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, Integer>{
	List<CustomerDocument> findByCustomerUserId(int customerId);
	Page<CustomerDocument> findByIsVerifiedFalseAndIsDeletedFalse(Pageable pageable);
}
