package com.example.demo.dto.loan;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.example.demo.model.Financials;

import lombok.Data;

@Data
public class LoanUpdateDTO {

    @NotBlank
    private String clientName;

    @NotBlank
    private String loanType;

    @NotNull
    @Min(1)
    private Long requestedAmount;

    @NotNull
    private Double proposedInterestRate;

    @NotNull
    private Integer tenureMonths;

    @NotNull
    private Financials financials;
}
