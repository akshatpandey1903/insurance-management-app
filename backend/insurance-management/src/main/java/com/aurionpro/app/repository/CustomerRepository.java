package com.aurionpro.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.app.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
}
