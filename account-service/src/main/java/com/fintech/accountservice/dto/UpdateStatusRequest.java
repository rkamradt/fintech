package com.fintech.accountservice.dto;

import com.fintech.accountservice.model.AccountStatus;

public record UpdateStatusRequest(
        AccountStatus status,
        String authorizedBy
) {}
