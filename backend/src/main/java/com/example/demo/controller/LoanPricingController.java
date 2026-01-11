package com.example.demo.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.loan.LoanPricingDTO;
import com.example.demo.model.Loan;
import com.example.demo.services.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanPricingController {

    private final LoanService loanService;

    public LoanPricingController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/{id}/price")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Loan> priceLoan(
            @PathVariable String id,
            @RequestBody LoanPricingDTO dto,
            Principal principal
    ) {
        return ResponseEntity.ok(
                loanService.priceLoan(id, dto, principal.getName())
        );
    }
}
