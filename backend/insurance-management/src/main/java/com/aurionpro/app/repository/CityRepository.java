package com.aurionpro.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aurionpro.app.entity.City;

public interface CityRepository extends JpaRepository<City, Integer>{
	List<City> findByStateStateId(int id);
}