package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aurionpro.app.entity.State;

public interface StateRepository extends JpaRepository<State, Integer>{
	@Query("SELECT u FROM State u WHERE u.isDeleted = false")
    List<State> findAll();
}
