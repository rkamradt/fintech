package com.fintech.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.userservice.dto.CreateUserRequest;
import com.fintech.userservice.dto.UserResponse;
import com.fintech.userservice.model.User;
import com.fintech.userservice.model.UserRole;
import com.fintech.userservice.model.UserStatus;
import com.fintech.userservice.repository.UserRepository;
import com.fintech.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    private UUID setupManagerUser() {
        UUID managerId = UUID.randomUUID();
        User manager = new User();
        manager.setUsername("manager");
        manager.setRole(UserRole.MANAGER);
        manager.setStatus(UserStatus.ACTIVE);
        when(userRepository.findById(managerId)).thenReturn(Optional.of(manager));
        return managerId;
    }

    private UserResponse sampleUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        return new UserResponse(user);
    }

    @Test
    void createUser_requiresManagerRole() throws Exception {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("newuser");
        req.setRole(UserRole.CUSTOMER);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createUser_succeeds_withManagerHeader() throws Exception {
        UUID managerId = setupManagerUser();
        when(userService.createUser(any(), any())).thenReturn(sampleUser());

        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("newuser");
        req.setRole(UserRole.CUSTOMER);

        mockMvc.perform(post("/users")
                        .header("X-User-ID", managerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void getUser_requiresAuthentication() throws Exception {
        mockMvc.perform(get("/users/" + UUID.randomUUID()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUser_succeeds_withManagerHeader() throws Exception {
        UUID managerId = setupManagerUser();
        UUID userId = UUID.randomUUID();
        when(userService.getUser(eq(userId))).thenReturn(sampleUser());

        mockMvc.perform(get("/users/" + userId)
                        .header("X-User-ID", managerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
}
