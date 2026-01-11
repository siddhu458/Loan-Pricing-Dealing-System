package com.example.demo.services;

import java.time.Instant;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.loan.LoanCreateDTO;
import com.example.demo.dto.loan.LoanPricingDTO;
import com.example.demo.dto.loan.LoanStatusUpdateDTO;
import com.example.demo.dto.loan.LoanUpdateDTO;
import com.example.demo.model.AuditAction;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanStatus;
import com.example.demo.repository.LoanRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoanService {

    private final LoanRepository repo;

    // CREATE (DRAFT)
    public Loan create(LoanCreateDTO dto, String userId) {

        Loan loan = Loan.builder()
                .clientName(dto.getClientName())
                .loanType(dto.getLoanType())
                .requestedAmount(dto.getRequestedAmount())
                .tenureMonths(dto.getTenureMonths())
                .status(LoanStatus.DRAFT)
                .createdBy(userId)
                .createdAt(Instant.now())
                .build();

        addAudit(loan, userId, AuditAction.CREATED, "Loan draft created");

        return repo.save(loan);
    }

    // UPDATE DRAFT
    public Loan updateDraft(String loanId, LoanUpdateDTO dto, String userEmail) {

        Loan loan = repo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT loans can be edited");
        }

        loan.setClientName(dto.getClientName());
        loan.setLoanType(dto.getLoanType());
        loan.setRequestedAmount(dto.getRequestedAmount());
        loan.setProposedInterestRate(dto.getProposedInterestRate());
        loan.setTenureMonths(dto.getTenureMonths());
        loan.setFinancials(dto.getFinancials());
        loan.setUpdatedBy(userEmail);
        loan.setUpdatedAt(Instant.now());

        addAudit(loan, userEmail, AuditAction.UPDATED, "Loan draft updated");

        return repo.save(loan);
    }

    // SUBMIT (USER)
    public void submitLoan(String loanId, String userEmail) {

        Loan loan = repo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT loans can be submitted");
        }

        loan.setStatus(LoanStatus.SUBMITTED);
        loan.setUpdatedBy(userEmail);
        loan.setUpdatedAt(Instant.now());

        addAudit(loan, userEmail, AuditAction.SUBMITTED, "Loan submitted for approval");

        repo.save(loan);
    }

    //  ADMIN STATUS UPDATE
    public Loan updateStatus(
            String loanId,
            LoanStatusUpdateDTO dto,
            String adminEmail
    ) {

        Loan loan = repo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        LoanStatus nextStatus = LoanStatus.valueOf(dto.getStatus());

        if (loan.getStatus() == LoanStatus.SUBMITTED &&
                nextStatus == LoanStatus.UNDER_REVIEW) {

            loan.setStatus(LoanStatus.UNDER_REVIEW);

        } else if (loan.getStatus() == LoanStatus.UNDER_REVIEW &&
                (nextStatus == LoanStatus.APPROVED || nextStatus == LoanStatus.REJECTED)) {

            loan.setStatus(nextStatus);

        } else {
            throw new IllegalStateException("Invalid status transition");
        }

        loan.setApprovedBy(adminEmail);
        loan.setApprovedAt(Instant.now());
        loan.setUpdatedAt(Instant.now());

        addAudit(loan, adminEmail, nextStatus.toAuditAction(), dto.getComments());

        return repo.save(loan);
    }

    // LIST
    public Page<Loan> findAll(Pageable pageable) {
        return repo.findByDeletedFalse(pageable);
    }

    //  AUDIT HELPER
    private void addAudit(Loan loan, String by, AuditAction action, String comments) {

        log.info("Actions list before add: {}", loan.getActions());

        if (loan.getActions() == null) {
            loan.setActions(new ArrayList<>());
        }

        loan.getActions().add(
                com.example.demo.model.Audit.builder()
                        .by(by)
                        .action(action)
                        .comments(comments)
                        .timestamp(Instant.now())
                        .build()
        );
    }
    
    
    // delete it 
    private final PricingService pricingService;

    public LoanService(LoanRepository repo, PricingService pricingService) {
        this.repo = repo;
        this.pricingService = pricingService;
    }
    
    public Loan priceLoan(String loanId, LoanPricingDTO dto, String userEmail) {

        Loan loan = repo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.DRAFT) {
            throw new IllegalStateException("Pricing allowed only in DRAFT status");
        }

        if (loan.getProposedInterestRate() != null) {
            throw new IllegalStateException("Loan already priced");
        }

        double finalRate = pricingService.calculateInterestRate(
                dto.getBaseRate(),
                loan.getRequestedAmount(),
                loan.getTenureMonths(),
                dto.getCreditRating()
        );

        loan.setProposedInterestRate(finalRate);
        loan.setUpdatedBy(userEmail);
        loan.setUpdatedAt(Instant.now());

        addAudit(
                loan,
                userEmail,
                AuditAction.PRICED,
                "Loan priced using credit rating " + dto.getCreditRating()
        );

        return repo.save(loan);
    }


 // SOFT DELETE (ADMIN)
    public void softDeleteLoan(String loanId, String adminEmail) {

        Loan loan = repo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.isDeleted()) {
            throw new IllegalStateException("Loan already deleted");
        }

        loan.setDeleted(true);
        loan.setDeletedAt(Instant.now());
        loan.setUpdatedBy(adminEmail);
        loan.setUpdatedAt(Instant.now());

        addAudit(
                loan,
                adminEmail,
                AuditAction.DELETED,
                "Loan soft deleted by admin"
        );

        repo.save(loan);
    }

}
