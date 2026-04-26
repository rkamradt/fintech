package com.fintech.transactionservice.event;

import com.fintech.transactionservice.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionInitiatedEvent(
        String transactionId,
        String accountId,
        BigDecimal amount,
        TransactionType type,
        String authenticatedUserId,
        String customerIdOnBehalf,
        Instant timestamp
) {}
