package com.fintech.userservice.dto;

import com.fintech.userservice.model.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {
    @NotNull
    private UserStatus status;
}
