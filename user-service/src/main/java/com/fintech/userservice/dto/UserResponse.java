package com.fintech.userservice.dto;

import com.fintech.userservice.model.User;
import com.fintech.userservice.model.UserRole;
import com.fintech.userservice.model.UserStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserResponse {
    private final UUID id;
    private final String username;
    private final UserRole role;
    private final UserStatus status;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Instant createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
}
