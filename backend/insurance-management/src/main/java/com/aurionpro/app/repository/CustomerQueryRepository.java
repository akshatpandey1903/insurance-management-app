package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.app.entity.CustomerQuery;
import com.aurionpro.app.entity.QueryStatus;

public interface CustomerQueryRepository extends JpaRepository<CustomerQuery, Integer>{
	List<CustomerQuery> findAllByCustomer_UserIdAndIsDeletedFalse(int customerId);

	Page<CustomerQuery> findByStatus(QueryStatus status, Pageable pageable);
	Page<CustomerQuery> findAll(Pageable pageable);
	Page<CustomerQuery> findByStatusAndIsDeletedFalse(QueryStatus status, Pageable pageable);
    
    Page<CustomerQuery> findAllByIsDeletedFalse(Pageable pageable);
}
