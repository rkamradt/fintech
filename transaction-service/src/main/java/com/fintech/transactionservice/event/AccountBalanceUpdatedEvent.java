package com.fintech.transactionservice.event;

import java.math.BigDecimal;

public record AccountBalanceUpdatedEvent(
        String accountId,
        BigDecimal newBalance,
        BigDecimal previousBalance,
        String transactionId
) {}
