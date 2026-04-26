package com.fintech.transactionservice.event;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionCompletedEvent(
        String transactionId,
        String accountId,
        BigDecimal amount,
        BigDecimal newBalance,
        String authenticatedUserId,
        Instant timestamp
) {}
