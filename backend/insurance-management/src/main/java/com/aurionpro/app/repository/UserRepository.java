package com.aurionpro.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aurionpro.app.entity.Role;
import com.aurionpro.app.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
	@Query("SELECT u FROM User u WHERE u.isActive = true")
    List<User> findAll();
	Page<User> findAllByRole(Role role, Pageable pageable);
}
