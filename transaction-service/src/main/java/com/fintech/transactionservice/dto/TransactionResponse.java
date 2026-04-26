package com.fintech.transactionservice.dto;

import com.fintech.transactionservice.domain.Transaction;
import com.fintech.transactionservice.domain.TransactionStatus;
import com.fintech.transactionservice.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponse(
        String transactionId,
        String accountId,
        String fromAccountId,
        String toAccountId,
        BigDecimal amount,
        TransactionType type,
        TransactionStatus status,
        String authenticatedUserId,
        String customerIdOnBehalf,
        BigDecimal newBalance,
        String failureReason,
        Instant createdAt,
        Instant completedAt
) {
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(
                t.getTransactionId(),
                t.getAccountId(),
                t.getFromAccountId(),
                t.getToAccountId(),
                t.getAmount(),
                t.getType(),
                t.getStatus(),
                t.getAuthenticatedUserId(),
                t.getCustomerIdOnBehalf(),
                t.getNewBalance(),
                t.getFailureReason(),
                t.getCreatedAt(),
                t.getCompletedAt()
        );
    }
}
