package com.fintech.accountservice.dto;

import java.math.BigDecimal;

public record UpdateBalanceRequest(
        BigDecimal newBalance,
        String transactionId
) {}
