package com.example.demo.services;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Audit;
import com.example.demo.model.AuditAction;
import com.example.demo.repository.AuditRepository;

@Service
public class AuditService {

    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void logAudit(String loanId, String userEmail, AuditAction action, String comments) {
        Audit audit = Audit.builder()
                .loanId(loanId)
                .by(userEmail)
                .action(action)
                .comments(comments)
                .timestamp(Instant.now())
                .build();
        
        auditRepository.save(audit);
    }

    public List<Audit> getAllAuditLogs() {
        return auditRepository.findAll();
    }

    public List<Audit> getAuditLogsForLoan(String loanId) {
        return auditRepository.findByLoanIdOrderByTimestampDesc(loanId);
    }
}
