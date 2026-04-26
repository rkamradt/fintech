package com.fintech.accountservice.kafka.event;

import java.math.BigDecimal;

public record AccountBalanceUpdatedEvent(
        String accountId,
        BigDecimal newBalance,
        BigDecimal previousBalance,
        String transactionId
) {}
