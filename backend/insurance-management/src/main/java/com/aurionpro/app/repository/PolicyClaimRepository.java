package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.aurionpro.app.entity.CustomerPolicy;
import com.aurionpro.app.entity.PolicyClaim;

public interface PolicyClaimRepository extends JpaRepository<PolicyClaim, Integer>, JpaSpecificationExecutor<PolicyClaim> {
    boolean existsByCustomerPolicyAndIsDeletedFalse(CustomerPolicy policy);
    List<PolicyClaim> findByCustomerPolicy_Customer_UserIdAndIsDeletedFalse(int customerId);
}