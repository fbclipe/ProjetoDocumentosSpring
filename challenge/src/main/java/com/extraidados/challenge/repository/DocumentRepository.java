package com.extraidados.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.extraidados.challenge.entity.Documents;

@Repository
public interface DocumentRepository extends JpaRepository<Documents, Long> {
}
