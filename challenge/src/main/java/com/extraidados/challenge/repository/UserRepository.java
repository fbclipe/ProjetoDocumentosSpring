package com.extraidados.challenge.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.extraidados.challenge.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByAuthToken(String authToken);
} 
