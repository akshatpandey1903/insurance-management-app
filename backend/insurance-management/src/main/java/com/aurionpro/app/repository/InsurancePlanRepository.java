package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aurionpro.app.entity.InsurancePlan;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan, Integer>{
	@Query("SELECT u FROM InsurancePlan u WHERE u.isActive = true")
    List<InsurancePlan> findAll();
}
