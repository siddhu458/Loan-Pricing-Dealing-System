package com.example.demo.exceptions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandlerTest.TestControllerConfig.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // REGISTER TEST CONTROLLER 

    @TestConfiguration
    static class TestControllerConfig {

        @RestController
        static class TestExceptionController {

            @GetMapping("/test/runtime-exception")
            public String throwRuntimeException() {
                throw new RuntimeException("Something went wrong");
            }
        }
    }

    // TEST

    @Test
    @WithMockUser(username = "user@mail.com", roles = "USER")
    void handleRuntimeException_returnsBadRequestAndErrorResponse() throws Exception {

        mockMvc.perform(get("/test/runtime-exception")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Something went wrong"))
                .andExpect(jsonPath("$.path").value("/test/runtime-exception"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
