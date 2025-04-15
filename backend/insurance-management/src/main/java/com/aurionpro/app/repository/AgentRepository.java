package com.aurionpro.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aurionpro.app.dto.AgentReportResponseDTO;
import com.aurionpro.app.entity.Agent;

public interface AgentRepository extends JpaRepository<Agent, Integer>{
	Page<Agent> findByIsApprovedFalseAndIsActiveTrue(Pageable pageable);
	Page<Agent> findByIsActiveTrue(Pageable pageable);
	Optional<Agent> findByLicenseNumberAndIsActiveTrue(String licenseNumber);
	Optional<Agent> findByUserId(int userId);
	
	@Query("SELECT new com.aurionpro.app.dto.AgentReportResponseDTO(" +
		       "a.userId, " +
		       "CONCAT(a.firstName, ' ', a.lastName), " +
		       "a.email, " +
		       "CASE WHEN a.approvedBy IS NULL THEN 'Not Approved' ELSE CONCAT(a.approvedBy.firstName, ' ', a.approvedBy.lastName) END, " +
		       "COUNT(cp), " +
		       "a.totalEarnings, " +
		       "a.createdAt) " +
		       "FROM Agent a LEFT JOIN a.soldPolicies cp " +
		       "GROUP BY a.userId, a.firstName, a.lastName, a.email, a.approvedBy, a.totalEarnings, a.createdAt")
		Page<AgentReportResponseDTO> getAgentReport(Pageable pageable);


		@Query("SELECT new com.aurionpro.app.dto.AgentReportResponseDTO(" +
		       "a.userId, " +
		       "CONCAT(a.firstName, ' ', a.lastName), " +
		       "a.email, " +
		       "CASE WHEN a.approvedBy IS NULL THEN 'Not Approved' ELSE CONCAT(a.approvedBy.firstName, ' ', a.approvedBy.lastName) END, " +
		       "COUNT(cp), " +
		       "a.totalEarnings, " +
		       "a.createdAt) " +
		       "FROM Agent a LEFT JOIN a.soldPolicies cp " +
		       "WHERE a.isActive = true AND (" +
		       "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(a.licenseNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "CONCAT(a.userId, '') LIKE CONCAT('%', :keyword, '%'))" +
		       "GROUP BY a.userId, a.firstName, a.lastName, a.email, a.approvedBy, a.totalEarnings, a.createdAt")
		Page<AgentReportResponseDTO> searchAgentReportByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
