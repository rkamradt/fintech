package com.fintech.transactionservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    private String transactionId;

    @Column(nullable = false)
    private String accountId;

    // For TRANSFER: source account
    private String fromAccountId;

    // For TRANSFER: destination account
    private String toAccountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    // The authenticated teller/manager who performed the operation
    @Column(nullable = false)
    private String authenticatedUserId;

    // The customer on whose behalf the operation was performed (optional)
    private String customerIdOnBehalf;

    // Balance after this transaction completed
    private BigDecimal newBalance;

    private String failureReason;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant completedAt;

    public static Transaction create(String accountId, BigDecimal amount, TransactionType type,
                                     String authenticatedUserId, String customerIdOnBehalf) {
        Transaction t = new Transaction();
        t.transactionId = UUID.randomUUID().toString();
        t.accountId = accountId;
        t.amount = amount;
        t.type = type;
        t.status = TransactionStatus.PENDING;
        t.authenticatedUserId = authenticatedUserId;
        t.customerIdOnBehalf = customerIdOnBehalf;
        t.createdAt = Instant.now();
        return t;
    }
}
