package com.example.demo.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.Loan;

public interface LoanRepository extends MongoRepository<Loan, String> {

    List<Loan> findByDeletedFalse();
    
    Page<Loan> findByDeletedFalse(Pageable pageable);
}
