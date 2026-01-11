package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.Audit;

public interface AuditRepository extends MongoRepository<Audit, String> {

    List<Audit> findByLoanIdOrderByTimestampDesc(String loanId);
}
