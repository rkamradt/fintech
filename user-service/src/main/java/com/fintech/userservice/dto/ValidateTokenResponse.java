package com.fintech.userservice.dto;

import com.fintech.userservice.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ValidateTokenResponse {
    private UUID userId;
    private String username;
    private UserRole role;
    private boolean valid;
}
