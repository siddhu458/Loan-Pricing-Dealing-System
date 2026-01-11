package com.example.demo.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Audit {

    @Id
    private String id;
    private String loanId;        
    private String by;              
    private AuditAction action;     
    private String comments;        
    private Instant timestamp;      
}
