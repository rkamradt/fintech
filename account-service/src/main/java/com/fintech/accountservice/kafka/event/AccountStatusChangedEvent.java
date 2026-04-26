package com.fintech.accountservice.kafka.event;

import com.fintech.accountservice.model.AccountStatus;

public record AccountStatusChangedEvent(
        String accountId,
        AccountStatus newStatus,
        AccountStatus previousStatus,
        String authorizedBy
) {}
