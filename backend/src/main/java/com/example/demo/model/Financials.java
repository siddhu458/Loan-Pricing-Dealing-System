package com.example.demo.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Financials {
    private Double emi;
    private Double totalInterest;
    private Double totalPayable;
}
