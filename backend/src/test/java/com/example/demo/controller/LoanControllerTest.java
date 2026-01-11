package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.config.JwtAuthenticationFilter;
import com.example.demo.config.JwtTokenProvider;
import com.example.demo.dto.loan.LoanCreateDTO;
import com.example.demo.dto.loan.LoanStatusUpdateDTO;
import com.example.demo.dto.loan.LoanUpdateDTO;
import com.example.demo.model.Financials;
import com.example.demo.model.Loan;
import com.example.demo.model.LoanStatus;
import com.example.demo.services.LoanService;
import com.example.demo.services.PricingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = LoanController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;
    
    @MockBean
    private PricingService pricingService;
    
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    // CREATE

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void createLoan_success() throws Exception {

        LoanCreateDTO dto = new LoanCreateDTO();
        dto.setClientName("John");
        dto.setLoanType("HOME");
        dto.setRequestedAmount(500000L);
        dto.setProposedInterestRate(8.5);
        dto.setTenureMonths(120);

        when(loanService.create(any(), anyString()))
                .thenReturn(Loan.builder().id("1").build());

        mockMvc.perform(post("/api/loans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    // UPDATE DRAFT

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void updateDraft_success() throws Exception {

        LoanUpdateDTO dto = new LoanUpdateDTO();
        dto.setClientName("Updated");
        dto.setLoanType("CAR");
        dto.setRequestedAmount(300000L);   
        dto.setProposedInterestRate(9.0);
        dto.setTenureMonths(60);
        
        Financials financials = Financials.builder()
                .emi(7500.0)
                .totalInterest(150000.0)
                .totalPayable(450000.0)
                .build();
        dto.setFinancials(financials);

        when(loanService.updateDraft(anyString(), any(), anyString()))
                .thenReturn(Loan.builder().build());

        mockMvc.perform(put("/api/loans/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }


    // SUBMIT

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void submitLoan_success() throws Exception {

        doNothing().when(loanService).submitLoan(anyString(), anyString());

        mockMvc.perform(patch("/api/loans/{id}/submit", "1"))
                .andExpect(status().isOk());
    }

    // UPDATE STATUS

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateStatus_success() throws Exception {

        LoanStatusUpdateDTO dto = new LoanStatusUpdateDTO();
        dto.setStatus("APPROVED");
        dto.setComments("ok");

        when(loanService.updateStatus(anyString(), any(), anyString()))
                .thenReturn(Loan.builder().status(LoanStatus.APPROVED).build());

        mockMvc.perform(patch("/api/loans/{id}/status", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    // DELETE

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deleteLoan_success() throws Exception {

        doNothing().when(loanService).softDeleteLoan(anyString(), anyString());

        mockMvc.perform(delete("/api/loans/{id}", "1"))
                .andExpect(status().isOk());
    }

    // GET

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void getLoans_success() throws Exception {

        Page<Loan> page = new PageImpl<>(
                List.of(Loan.builder().id("1").build()),
                PageRequest.of(0, 5),
                1
        );

        when(loanService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk());
    }
}
