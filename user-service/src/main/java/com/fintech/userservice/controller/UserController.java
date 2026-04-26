package com.fintech.userservice.controller;

import com.fintech.userservice.dto.CreateUserRequest;
import com.fintech.userservice.dto.UpdateRoleRequest;
import com.fintech.userservice.dto.UpdateStatusRequest;
import com.fintech.userservice.dto.UserResponse;
import com.fintech.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /users — manager only (enforced in SecurityConfig)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest req,
                                   Authentication auth) {
        return userService.createUser(req, auth.getName());
    }

    // GET /users/{userId}
    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable UUID userId) {
        return userService.getUser(userId);
    }

    // PUT /users/{userId}/role — manager only (enforced in SecurityConfig)
    @PutMapping("/{userId}/role")
    public UserResponse updateRole(@PathVariable UUID userId,
                                   @Valid @RequestBody UpdateRoleRequest req,
                                   Authentication auth) {
        return userService.updateRole(userId, req, auth.getName());
    }

    // PUT /users/{userId}/status — manager only (enforced in SecurityConfig)
    @PutMapping("/{userId}/status")
    public UserResponse updateStatus(@PathVariable UUID userId,
                                     @Valid @RequestBody UpdateStatusRequest req,
                                     Authentication auth) {
        return userService.updateStatus(userId, req, auth.getName());
    }

    // GET /users/tellers — manager only (enforced in SecurityConfig)
    @GetMapping("/tellers")
    public List<UserResponse> getTellers() {
        return userService.getTellers();
    }

    // GET /users/customers — teller+ (enforced in SecurityConfig)
    @GetMapping("/customers")
    public List<UserResponse> getCustomers() {
        return userService.getCustomers();
    }
}
