package com.fintech.accountservice.dto;

import com.fintech.accountservice.model.AccountType;
import java.math.BigDecimal;

public record CreateAccountRequest(
        String customerId,
        AccountType accountType,
        BigDecimal initialBalance
) {}
