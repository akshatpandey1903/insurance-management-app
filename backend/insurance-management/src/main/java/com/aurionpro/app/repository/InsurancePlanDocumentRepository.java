package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.app.entity.InsurancePlan;
import com.aurionpro.app.entity.InsurancePlanDocument;

public interface InsurancePlanDocumentRepository extends JpaRepository<InsurancePlanDocument, Integer>{
	List<InsurancePlanDocument> findByInsurancePlan(InsurancePlan plan);
    void deleteByInsurancePlan(InsurancePlan plan);
}
