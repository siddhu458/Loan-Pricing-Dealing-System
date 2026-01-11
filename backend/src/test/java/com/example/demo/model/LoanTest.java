package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class LoanTest {

    @Test
    void testLoanBuilderAndGetters() {
        Instant now = Instant.now();

        Loan loan = Loan.builder()
                .id("loan123")
                .clientName("John Doe")
                .loanType("HOME")
                .requestedAmount(500000L)
                .proposedInterestRate(8.5)
                .tenureMonths(120)
                .status(LoanStatus.DRAFT)
                .createdBy("user1")
                .createdAt(now)
                .build();

        assertNotNull(loan);
        assertEquals("loan123", loan.getId());
        assertEquals("John Doe", loan.getClientName());
        assertEquals("HOME", loan.getLoanType());
        assertEquals(500000L, loan.getRequestedAmount());
        assertEquals(8.5, loan.getProposedInterestRate());
        assertEquals(120, loan.getTenureMonths());
        assertEquals(LoanStatus.DRAFT, loan.getStatus());
        assertEquals("user1", loan.getCreatedBy());
        assertEquals(now, loan.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
    	Loan loan = Loan.builder().build();

        loan.setId("loan001");
        loan.setClientName("Alice");
        loan.setLoanType("CAR");
        loan.setRequestedAmount(300000L);
        loan.setProposedInterestRate(9.2);
        loan.setTenureMonths(60);
        loan.setDeleted(true);

        assertEquals("loan001", loan.getId());
        assertEquals("Alice", loan.getClientName());
        assertEquals("CAR", loan.getLoanType());
        assertEquals(300000L, loan.getRequestedAmount());
        assertEquals(9.2, loan.getProposedInterestRate());
        assertEquals(60, loan.getTenureMonths());
        assertTrue(loan.isDeleted());
    }

    @Test
    void testDefaultActionsList() {
    	Loan loan = Loan.builder().build();

        assertNotNull(loan.getActions());
        assertTrue(loan.getActions().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        Loan loan1 = Loan.builder()
                .id("sameId")
                .clientName("User1")
                .build();

        Loan loan2 = Loan.builder()
                .id("sameId")
                .clientName("User1")
                .build();

        assertEquals(loan1, loan2);
        assertEquals(loan1.hashCode(), loan2.hashCode());
    }

    @Test
    void testToString() {
        Loan loan = Loan.builder()
                .id("loanToString")
                .clientName("Test User")
                .build();

        String toString = loan.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("loanToString"));
        assertTrue(toString.contains("Test User"));
    }

    @Test
    void testApprovedFields() {
        Instant approvedAt = Instant.now();

        Loan loan = Loan.builder().build();
        loan.setSanctionedAmount(450000L);
        loan.setApprovedInterestRate(7.9);
        loan.setApprovedBy("admin1");
        loan.setApprovedAt(approvedAt);

        assertEquals(450000L, loan.getSanctionedAmount());
        assertEquals(7.9, loan.getApprovedInterestRate());
        assertEquals("admin1", loan.getApprovedBy());
        assertEquals(approvedAt, loan.getApprovedAt());
    }
}
