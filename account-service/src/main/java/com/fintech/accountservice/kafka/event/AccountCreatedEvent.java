package com.fintech.accountservice.kafka.event;

import com.fintech.accountservice.model.AccountType;
import java.math.BigDecimal;

public record AccountCreatedEvent(
        String accountId,
        String customerId,
        AccountType accountType,
        BigDecimal initialBalance
) {}
