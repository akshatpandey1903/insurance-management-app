package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.CustomerDocument;
import com.aurionpro.app.entity.DocumentStatus;

public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, Integer>{
	List<CustomerDocument> findByCustomerUserId(int customerId);
	Page<CustomerDocument> findByStatusAndIsDeletedFalse(DocumentStatus status, Pageable pageable);
	List<CustomerDocument> findByCustomerAndStatus(Customer customer, DocumentStatus status);

}
