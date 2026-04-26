package com.fintech.userservice.controller;

import com.fintech.userservice.dto.LoginRequest;
import com.fintech.userservice.dto.LoginResponse;
import com.fintech.userservice.dto.ValidateTokenRequest;
import com.fintech.userservice.dto.ValidateTokenResponse;
import com.fintech.userservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /auth/login — public
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    // POST /auth/validate — public (used by other services)
    @PostMapping("/validate")
    public ValidateTokenResponse validate(@Valid @RequestBody ValidateTokenRequest req) {
        return authService.validate(req);
    }
}
