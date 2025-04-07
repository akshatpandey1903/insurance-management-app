package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aurionpro.app.entity.InsuranceType;

public interface InsuranceTypeRepository extends JpaRepository<InsuranceType, Integer>{
	
	@Query("SELECT u FROM InsuranceType u WHERE u.isDeleted = false")
    List<InsuranceType> findAll();
}	
