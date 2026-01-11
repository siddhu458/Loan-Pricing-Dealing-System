package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.demo.dto.loan.LoanCreateDTO;
import com.example.demo.dto.loan.LoanPricingDTO;
import com.example.demo.dto.loan.LoanStatusUpdateDTO;
import com.example.demo.dto.loan.LoanUpdateDTO;
import com.example.demo.model.AuditAction;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanStatus;
import com.example.demo.repository.LoanRepository;
import com.example.demo.services.LoanService;
import com.example.demo.services.PricingService;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository repo;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private LoanService loanService;

    private Loan draftLoan;

    @BeforeEach
    void setup() {
        draftLoan = Loan.builder()
                .id("loan1")
                .clientName("John")
                .loanType("HOME")
                .requestedAmount(500000L)
                .tenureMonths(120)
                .status(LoanStatus.DRAFT)
                .actions(new ArrayList<>()) 
                .build();
    }

    // CREATE

    @Test
    void createLoan_success() {
        LoanCreateDTO dto = new LoanCreateDTO();
        dto.setClientName("John");
        dto.setLoanType("HOME");
        dto.setRequestedAmount(500000L);
        dto.setTenureMonths(120);

        when(repo.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = loanService.create(dto, "user1");

        assertEquals(LoanStatus.DRAFT, loan.getStatus());
        assertEquals("user1", loan.getCreatedBy());
        assertNotNull(loan.getCreatedAt());
        assertFalse(loan.getActions().isEmpty());
        assertEquals(AuditAction.CREATED, loan.getActions().get(0).getAction());
    }

    // UPDATE DRAFT

    @Test
    void updateDraft_success() {
        LoanUpdateDTO dto = new LoanUpdateDTO();
        dto.setClientName("Updated");
        dto.setLoanType("CAR");
        dto.setRequestedAmount(300000L);
        dto.setProposedInterestRate(9.5);
        dto.setTenureMonths(60);

        when(repo.findById("loan1")).thenReturn(Optional.of(draftLoan));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan updated = loanService.updateDraft("loan1", dto, "user1");

        assertEquals("Updated", updated.getClientName());
        assertEquals("user1", updated.getUpdatedBy());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void updateDraft_notDraft_throwsException() {
        draftLoan.setStatus(LoanStatus.SUBMITTED);
        when(repo.findById("loan1")).thenReturn(Optional.of(draftLoan));

        assertThrows(IllegalStateException.class,
                () -> loanService.updateDraft("loan1", new LoanUpdateDTO(), "user1"));
    }

    // SUBMIT

    @Test
    void submitLoan_success() {
        when(repo.findById("loan1")).thenReturn(Optional.of(draftLoan));

        loanService.submitLoan("loan1", "user1");

        assertEquals(LoanStatus.SUBMITTED, draftLoan.getStatus());
        verify(repo).save(draftLoan);
    }

    @Test
    void submitLoan_notDraft_throwsException() {
        draftLoan.setStatus(LoanStatus.APPROVED);
        when(repo.findById("loan1")).thenReturn(Optional.of(draftLoan));

        assertThrows(IllegalStateException.class,
                () -> loanService.submitLoan("loan1", "user1"));
    }

    // STATUS UPDATE

    @Test
    void updateStatus_submitted_to_underReview() {
        draftLoan.setStatus(LoanStatus.SUBMITTED);

        LoanStatusUpdateDTO dto = new LoanStatusUpdateDTO();
        dto.setStatus("UNDER_REVIEW");

        when(repo.findById("loan1")).thenReturn(Optional.of(draftLoan));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = loanService.updateStatus("loan1", dto, "admin");

        assertEquals(LoanStatus.UNDER_REVIEW, loan.getStatus());
        assertEquals("admin", loan.getApprovedBy());
    }

    @Test
    void updateStatus_underReview_to_approved() {
        draftLoan.setStatus(LoanStatus.UNDER_REVIEW);

        LoanStatusUpdateDTO dto = new LoanStatusUpdateDTO();
        dto.setStatus("APPROVED");

        when(repo.findById("loan1")).thenReturn(Optional.of(draftLoan));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = loanService.updateStatus("loan1", dto, "admin");

        assertEquals(LoanStatus.APPROVED, loan.getStatus());
    }

    // PRICING

    @Test
    void priceLoan_success() {
        LoanPricingDTO dto = new LoanPricingDTO();
        dto.setBaseRate(7.0);
        dto.setCreditRating("GOOD");

        when(repo.findById("loan1")).thenReturn(Optional.of(draftLoan));
        when(pricingService.calculateInterestRate(
                anyDouble(), anyDouble(), anyInt(), anyString()))
                .thenReturn(8.2); 
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Loan priced = loanService.priceLoan("loan1", dto, "user1");

        assertEquals(8.2, priced.getProposedInterestRate());
        assertEquals("user1", priced.getUpdatedBy());
    }

    // FIND ALL

    @Test
    void findAll_success() {
        Page<Loan> page = new PageImpl<>(List.of(draftLoan));
        when(repo.findByDeletedFalse(any())).thenReturn(page);

        Page<Loan> result = loanService.findAll(PageRequest.of(0, 5));

        assertEquals(1, result.getTotalElements());
    }

    // SOFT DELETE

    @Test
    void softDelete_success() {
        when(repo.findById("loan1")).thenReturn(Optional.of(draftLoan));

        loanService.softDeleteLoan("loan1", "admin");

        assertTrue(draftLoan.isDeleted());
        verify(repo).save(draftLoan);
    }
}
