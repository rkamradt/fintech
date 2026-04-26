package com.fintech.userservice.dto;

import com.fintech.userservice.model.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private UserRole role;

    // Optional — populated for CUSTOMER role
    private String firstName;
    private String lastName;
    private String email;
}
