package com.extraidados.challenge.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.extraidados.challenge.entity.Documents;

@Repository
public interface DocumentRepository extends JpaRepository<Documents, Long> {
    List<Documents> findByDate (LocalDate date);
    List<Documents> findByDateBetween(LocalDate begin, LocalDate end);
}
