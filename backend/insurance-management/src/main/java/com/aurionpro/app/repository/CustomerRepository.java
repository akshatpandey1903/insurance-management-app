package com.aurionpro.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aurionpro.app.dto.CustomerReportDto;
import com.aurionpro.app.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	@Query("SELECT new com.aurionpro.app.dto.CustomerReportDto(" +
		       "c.userId, c.firstName, c.lastName, c.email, c.phoneNumber, COUNT(cp)) " +
		       "FROM Customer c LEFT JOIN c.customerPolicies cp " +
		       "GROUP BY c.userId, c.firstName, c.lastName, c.email, c.phoneNumber")
		Page<CustomerReportDto> getCustomerReport(Pageable pageable);
}
