package com.fintech.accountservice.kafka.event;

import java.math.BigDecimal;

public record TransactionCompletedEvent(
        String transactionId,
        String accountId,
        BigDecimal amount,
        BigDecimal newBalance,
        String authenticatedUserId
) {}
