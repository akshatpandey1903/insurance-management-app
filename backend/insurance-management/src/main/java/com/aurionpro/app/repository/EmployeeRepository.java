package com.aurionpro.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.app.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

}
