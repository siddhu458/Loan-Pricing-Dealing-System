package com.example.demo.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "loans")
public class Loan {

    @Id
    private String id;

    private String clientName;
    private String loanType;
    private Long requestedAmount;
    private Double proposedInterestRate;
    private Integer tenureMonths;
    private Financials financials;
    
    private LoanStatus status;

    private Long sanctionedAmount;
    private Double approvedInterestRate;

    private String createdBy;
    private String updatedBy;
    private String approvedBy;
    private Instant approvedAt;

    private Instant createdAt;
    private Instant updatedAt;

    private boolean deleted;
    private Instant deletedAt;
    
    @Builder.Default
    private List<Audit> actions = new ArrayList<>();
}
