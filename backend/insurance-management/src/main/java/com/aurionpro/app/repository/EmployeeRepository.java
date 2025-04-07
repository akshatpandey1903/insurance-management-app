package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aurionpro.app.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
	@Query("SELECT u FROM Employee u WHERE u.isActive = true")
    List<Employee> findAll();
}
