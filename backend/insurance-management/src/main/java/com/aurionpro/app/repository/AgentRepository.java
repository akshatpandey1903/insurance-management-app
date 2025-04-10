package com.aurionpro.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.app.entity.Agent;

public interface AgentRepository extends JpaRepository<Agent, Integer>{
	Page<Agent> findByIsApprovedFalseAndIsActiveTrue(Pageable pageable);
	Page<Agent> findByIsActiveTrue(Pageable pageable);
	Optional<Agent> findByLicenseNumberAndIsActiveTrue(String licenseNumber);
}
