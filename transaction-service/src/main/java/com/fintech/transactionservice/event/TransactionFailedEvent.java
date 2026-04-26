package com.fintech.transactionservice.event;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionFailedEvent(
        String transactionId,
        String accountId,
        BigDecimal amount,
        String reason,
        String authenticatedUserId,
        Instant timestamp
) {}
