package com.example.demo.dto.loan;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoanPricingDTO {

    @NotNull
    private Double baseRate;

    @NotNull
    private String creditRating;
}

