package com.codework.demohotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codework.demohotel.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String role);
	
	boolean existsByName(String role);
}
