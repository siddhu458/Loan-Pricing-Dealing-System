package com.example.demo.dto.user;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditTrailDTO {
    
    private String id;
    private String loanId;
    private String userEmail;
    private String action;
    private String comments;
    private Instant timestamp;
}
