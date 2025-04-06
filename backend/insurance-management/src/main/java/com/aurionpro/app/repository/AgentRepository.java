package com.aurionpro.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.app.entity.Agent;

public interface AgentRepository extends JpaRepository<Agent, Integer>{
	Page<Agent> findByIsApprovedFalse(Pageable pageable);
}
