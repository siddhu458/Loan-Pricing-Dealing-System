package com.example.demo.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.loan.LoanCreateDTO;
import com.example.demo.dto.loan.LoanStatusUpdateDTO;
import com.example.demo.dto.loan.LoanUpdateDTO;
import com.example.demo.model.Loan;
import com.example.demo.services.LoanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping
    public Loan create(
            @Valid @RequestBody LoanCreateDTO dto,
            Authentication auth
    ) {
        return service.create(dto, auth.getName());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Loan updateDraft(
            @PathVariable String id,
            @Valid @RequestBody LoanUpdateDTO dto,
            Principal principal
    ) {
        return service.updateDraft(id, dto, principal.getName());
    }

    @PatchMapping("/{id}/submit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> submitLoan(
            @PathVariable String id,
            Authentication auth
    ) {
        service.submitLoan(id, auth.getName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Loan updateStatus(
            @PathVariable String id,
            @RequestBody LoanStatusUpdateDTO dto,
            Authentication auth
    ) {
        return service.updateStatus(id, dto, auth.getName());
    }

    @GetMapping
    public Page<Loan> getLoans(
            @PageableDefault(page = 0, size = 5)
            Pageable pageable
    ) {
        return service.findAll(pageable);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLoan(
            @PathVariable String id,
            Authentication auth
    ) {
        service.softDeleteLoan(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

}
