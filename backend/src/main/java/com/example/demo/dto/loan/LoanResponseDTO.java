package com.example.demo.dto.loan;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.example.demo.model.LoanStatus;
import lombok.*;

@Data
@Builder
public class LoanResponseDTO {
	private String id;
	private String clientName;
	private String loanType;
	private Double requestedAmount;
	private Double proposedInterestRate;
	private Integer tenureMonths;
	private Map<String, Object> financials;
	
	private LoanStatus status;
	
	private Double sanctionedAmount;
	private Double approvedInterestRate;
	
	private String createdBy;
	private String approvedBy;
	private Instant approvedAt;
	
	private List<?> actions;
	
	private Instant createdAt;
	private Instant updatedAt;
}
