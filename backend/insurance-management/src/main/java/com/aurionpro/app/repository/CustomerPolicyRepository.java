package com.aurionpro.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aurionpro.app.dto.PlanPurchaseReportDto;
import com.aurionpro.app.entity.CustomerPolicy;

public interface CustomerPolicyRepository extends JpaRepository<CustomerPolicy, Integer>{
	
		@Query("SELECT new com.aurionpro.app.dto.PlanPurchaseReportDto(" +
		       "cp.id, " +
		       "CONCAT(c.firstName, ' ', c.lastName), " +
		       "it.typeName, " +
		       "ip.planName, " +
		       "cp.startDate, " +
		       "cp.calculatedPremium, " +
		       "CASE WHEN cp.isActive = true THEN 'Active' ELSE 'Inactive' END) " +
		       "FROM CustomerPolicy cp " +
		       "JOIN cp.customer c " +
		       "JOIN cp.insurancePlan ip " +
		       "JOIN ip.insuranceType it")
		Page<PlanPurchaseReportDto> fetchPlanPurchaseReport(Pageable pageable);


}
