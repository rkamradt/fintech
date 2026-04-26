package com.fintech.transactionservice.dto;

import java.math.BigDecimal;

public record AccountResponse(
        String accountId,
        String customerId,
        String accountType,
        BigDecimal balance,
        String status
) {}
