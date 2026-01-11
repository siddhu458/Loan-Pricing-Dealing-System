package com.example.demo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LoanStatusTest {

    @Test
    void toAuditAction_draft() {
        assertEquals(
                AuditAction.CREATED,
                LoanStatus.DRAFT.toAuditAction()
        );
    }

    @Test
    void toAuditAction_submitted() {
        assertEquals(
                AuditAction.SUBMITTED,
                LoanStatus.SUBMITTED.toAuditAction()
        );
    }

    @Test
    void toAuditAction_underReview() {
        assertEquals(
                AuditAction.UNDER_REVIEW,
                LoanStatus.UNDER_REVIEW.toAuditAction()
        );
    }

    @Test
    void toAuditAction_approved() {
        assertEquals(
                AuditAction.APPROVED,
                LoanStatus.APPROVED.toAuditAction()
        );
    }

    @Test
    void toAuditAction_rejected() {
        assertEquals(
                AuditAction.REJECTED,
                LoanStatus.REJECTED.toAuditAction()
        );
    }
}

