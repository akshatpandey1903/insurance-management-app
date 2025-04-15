package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aurionpro.app.dto.AgentCommissionReportDto;
import com.aurionpro.app.dto.PlanPurchaseReportDto;
import com.aurionpro.app.entity.Agent;
import com.aurionpro.app.entity.CustomerPolicy;

public interface CustomerPolicyRepository extends JpaRepository<CustomerPolicy, Integer>{
	
	List<CustomerPolicy> findByAgentAndIsActiveTrue(Agent agent);
	
		@Query("SELECT new com.aurionpro.app.dto.PlanPurchaseReportDto(" +
		       "cp.id, " +
		       "CONCAT(c.firstName, ' ', c.lastName), " +
		       "c.phoneNumber, " +
		       "it.typeName, " +
		       "ip.planName, " +
		       "cp.startDate, " +
		       "cp.endDate, " +
		       "cp.calculatedPremium, " +
		       "cp.paymentFrequency, " +
		       "CASE WHEN a IS NOT NULL THEN CONCAT(a.firstName, ' ', a.lastName) ELSE 'N/A' END, " +
		       "CASE WHEN cp.isActive = true THEN 'Active' ELSE 'Inactive' END) " +
		       "FROM CustomerPolicy cp " +
		       "JOIN cp.customer c " +
		       "JOIN cp.insurancePlan ip " +
		       "JOIN ip.insuranceType it " +
		       "LEFT JOIN cp.agent a")
		Page<PlanPurchaseReportDto> fetchPolicyPaymentReport(Pageable pageable);


		
		
		@Query("SELECT new com.aurionpro.app.dto.AgentCommissionReportDto(" +
			      "a.userId, a.firstName, a.lastName, a.email, " +
			      "COUNT(cp), " +
			      "CAST(SUM(cp.calculatedPremium * cp.insurancePlan.commissionRate / 100) AS java.math.BigDecimal), " +
			      "a.totalEarnings) " +
			      "FROM Agent a LEFT JOIN a.soldPolicies cp " +
			      "GROUP BY a.userId, a.firstName, a.lastName, a.email, a.totalEarnings")
			Page<AgentCommissionReportDto> getAgentCommissionReport(Pageable pageable);

		Page<CustomerPolicy> findByIsActiveFalseAndApprovedByNullAndIsRejectedFalse(PageRequest of);
		
		@Query("SELECT cp FROM CustomerPolicy cp WHERE cp.agent.licenseNumber = :license")
		List<CustomerPolicy> findAllByAgentLicense(String license);
		
		List<CustomerPolicy> findByCustomerUserIdAndIsCancelledFalse(int customerId);

}
