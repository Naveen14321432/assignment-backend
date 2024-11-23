package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	boolean existsByUsername(String string);
	boolean existsByEmail(String string);
	@Transactional
	void deleteByUsername(String id);
	Optional<User> findByUsername(String id);
	
}
