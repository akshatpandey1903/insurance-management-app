package com.aurionpro.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aurionpro.app.dto.WithdrawalReportDto;
import com.aurionpro.app.entity.WithdrawalRequest;

public interface WithdrawalRequestRepository extends JpaRepository<WithdrawalRequest, Integer>{
	
	@Query("""
	        SELECT new com.aurionpro.app.dto.WithdrawalReportDto(
	            w.withdrawalId,
	            CONCAT(u.firstName, ' ', u.lastName),
	            u.role.roleName, 
	            w.amount,
	            w.status,
	            w.requestedAt,
	            CASE WHEN e IS NOT NULL THEN CONCAT(e.firstName, ' ', e.lastName) ELSE NULL END,
	            w.approvedAt,
	            w.remarks
	        )
	        FROM WithdrawalRequest w
	        JOIN w.requestedBy u
	        LEFT JOIN w.approvedBy e
	    """)
	Page<WithdrawalReportDto> fetchWithdrawalReport(Pageable pageable);


}
