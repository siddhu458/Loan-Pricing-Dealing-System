package com.example.demo.dto.loan;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatusUpdateDTO {
	
	@NotBlank
	private String status; // SUBMITTED / APPROVED / REJECTED
	
	private String comments;
	

}
