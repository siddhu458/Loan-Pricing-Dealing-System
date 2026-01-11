package com.example.demo.dto.loan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanCreateDTO {

    @NotBlank
    private String clientName;

    @NotBlank
    private String loanType;

    @NotNull
    @Min(1)
    private Long requestedAmount;

    @NotNull
    @Min(1)
    private Double proposedInterestRate;

    @NotNull
    @Min(1)
    private Integer tenureMonths;
}
