package com.fintech.userservice.dto;

import com.fintech.userservice.model.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequest {
    @NotNull
    private UserRole role;
}
