package com.example.demo.model;

public enum LoanStatus {
	DRAFT,
	SUBMITTED,
	UNDER_REVIEW,
	APPROVED,
	REJECTED;
	
	 // MAP STATUS → AUDIT ACTION
    public AuditAction toAuditAction() {
        return switch (this) {
            case DRAFT -> AuditAction.CREATED;
            case SUBMITTED -> AuditAction.SUBMITTED;
            case UNDER_REVIEW -> AuditAction.UNDER_REVIEW;
            case APPROVED -> AuditAction.APPROVED;
            case REJECTED -> AuditAction.REJECTED;
        };
    }
}
