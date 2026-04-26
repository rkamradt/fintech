package com.fintech.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.userservice.dto.LoginRequest;
import com.fintech.userservice.dto.LoginResponse;
import com.fintech.userservice.dto.ValidateTokenRequest;
import com.fintech.userservice.model.UserRole;
import com.fintech.userservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void login_returnsToken() throws Exception {
        UUID userId = UUID.randomUUID();
        when(authService.login(any())).thenReturn(
                new LoginResponse("test-token", userId, "admin", UserRole.MANAGER));

        LoginRequest req = new LoginRequest();
        req.setUsername("admin");
        req.setPassword("admin");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.role").value("MANAGER"));
    }

    @Test
    void validate_returnsFalseForInvalidToken() throws Exception {
        com.fintech.userservice.dto.ValidateTokenResponse invalid =
                new com.fintech.userservice.dto.ValidateTokenResponse(null, null, null, false);
        when(authService.validate(any())).thenReturn(invalid);

        ValidateTokenRequest req = new ValidateTokenRequest();
        req.setToken("bad-token");

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }
}
