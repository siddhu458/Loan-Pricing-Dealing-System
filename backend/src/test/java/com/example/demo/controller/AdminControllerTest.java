package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.example.demo.model.User;
import com.example.demo.services.UserService;
import com.example.demo.services.AuditService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuditService auditService; 

    /* ---------------- LIST USERS ---------------- */

    @Test
    @WithMockUser(username = "admin@mail.com", roles = "ADMIN")
    void listUsers_success() throws Exception {

        User user1 = new User();
        user1.setId("1");
        user1.setEmail("user1@mail.com");
        user1.setActive(true);

        User user2 = new User();
        user2.setId("2");
        user2.setEmail("user2@mail.com");
        user2.setActive(false);

        when(userService.findAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/admin/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user1@mail.com"))
                .andExpect(jsonPath("$[1].email").value("user2@mail.com"));
    }

    /* ---------------- UPDATE STATUS ---------------- */

    @Test
    @WithMockUser(username = "admin@mail.com", roles = "ADMIN")
    void updateUserStatus_success() throws Exception {

        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setEmail("user1@mail.com");
        updatedUser.setActive(false);

        when(userService.updateStatus(anyString(), anyBoolean()))
                .thenReturn(updatedUser);

        doNothing().when(auditService).logAudit(
                anyString(),
                anyString(),
                any(),
                anyString()
        );

        mockMvc.perform(
                put("/api/admin/users/{id}/status", "1")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
                    .param("active", "false")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.active").value(false))
            .andExpect(jsonPath("$.email").value("user1@mail.com"));
    }
}
