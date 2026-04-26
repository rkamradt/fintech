package com.fintech.transactionservice.service;

import com.fintech.transactionservice.client.AccountServiceClient;
import com.fintech.transactionservice.domain.Transaction;
import com.fintech.transactionservice.domain.TransactionStatus;
import com.fintech.transactionservice.domain.TransactionType;
import com.fintech.transactionservice.dto.*;
import com.fintech.transactionservice.event.*;
import com.fintech.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountServiceClient accountServiceClient;
    private final TransactionEventProducer eventProducer;

    public TransactionService(TransactionRepository repository,
                              AccountServiceClient accountServiceClient,
                              TransactionEventProducer eventProducer) {
        this.repository = repository;
        this.accountServiceClient = accountServiceClient;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public TransactionResponse deposit(DepositRequest req, UserContext actor) {
        AccountResponse account = accountServiceClient.getAccount(req.accountId());

        Transaction tx = Transaction.create(
                req.accountId(), req.amount(), TransactionType.DEPOSIT,
                actor.userId(), req.customerIdOnBehalf()
        );
        repository.save(tx);

        eventProducer.publishInitiated(new TransactionInitiatedEvent(
                tx.getTransactionId(), tx.getAccountId(), tx.getAmount(), tx.getType(),
                actor.userId(), req.customerIdOnBehalf(), Instant.now()
        ));

        BigDecimal newBalance = account.balance().add(req.amount());
        complete(tx, newBalance, actor.userId());

        return TransactionResponse.from(tx);
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest req, UserContext actor) {
        AccountResponse account = accountServiceClient.getAccount(req.accountId());

        Transaction tx = Transaction.create(
                req.accountId(), req.amount(), TransactionType.WITHDRAW,
                actor.userId(), req.customerIdOnBehalf()
        );
        repository.save(tx);

        eventProducer.publishInitiated(new TransactionInitiatedEvent(
                tx.getTransactionId(), tx.getAccountId(), tx.getAmount(), tx.getType(),
                actor.userId(), req.customerIdOnBehalf(), Instant.now()
        ));

        if (account.balance().compareTo(req.amount()) < 0) {
            fail(tx, "Insufficient funds", actor.userId());
            return TransactionResponse.from(tx);
        }

        BigDecimal newBalance = account.balance().subtract(req.amount());
        complete(tx, newBalance, actor.userId());

        return TransactionResponse.from(tx);
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest req, UserContext actor) {
        AccountResponse fromAccount = accountServiceClient.getAccount(req.fromAccountId());

        Transaction tx = Transaction.create(
                req.fromAccountId(), req.amount(), TransactionType.TRANSFER,
                actor.userId(), null
        );
        tx.setFromAccountId(req.fromAccountId());
        tx.setToAccountId(req.toAccountId());
        repository.save(tx);

        eventProducer.publishInitiated(new TransactionInitiatedEvent(
                tx.getTransactionId(), tx.getAccountId(), tx.getAmount(), tx.getType(),
                actor.userId(), null, Instant.now()
        ));

        if (fromAccount.balance().compareTo(req.amount()) < 0) {
            fail(tx, "Insufficient funds in source account", actor.userId());
            return TransactionResponse.from(tx);
        }

        BigDecimal newBalance = fromAccount.balance().subtract(req.amount());
        complete(tx, newBalance, actor.userId());

        return TransactionResponse.from(tx);
    }

    public List<TransactionResponse> getByAccount(String accountId, UserContext actor) {
        List<Transaction> txns;
        if ("CUSTOMER".equalsIgnoreCase(actor.role())) {
            txns = repository.findByAccountIdAndCustomerIdOnBehalfOrderByCreatedAtDesc(accountId, actor.userId());
        } else {
            txns = repository.findByAccountIdOrderByCreatedAtDesc(accountId);
        }
        return txns.stream().map(TransactionResponse::from).toList();
    }

    public TransactionResponse getById(String transactionId) {
        return repository.findById(transactionId)
                .map(TransactionResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionId));
    }

    public List<TransactionResponse> getByUser(String userId) {
        return repository.findByAuthenticatedUserIdOrderByCreatedAtDesc(userId)
                .stream().map(TransactionResponse::from).toList();
    }

    private void complete(Transaction tx, BigDecimal newBalance, String authenticatedUserId) {
        tx.setStatus(TransactionStatus.COMPLETED);
        tx.setNewBalance(newBalance);
        tx.setCompletedAt(Instant.now());
        repository.save(tx);

        eventProducer.publishCompleted(new TransactionCompletedEvent(
                tx.getTransactionId(), tx.getAccountId(), tx.getAmount(),
                newBalance, authenticatedUserId, Instant.now()
        ));
    }

    private void fail(Transaction tx, String reason, String authenticatedUserId) {
        tx.setStatus(TransactionStatus.FAILED);
        tx.setFailureReason(reason);
        tx.setCompletedAt(Instant.now());
        repository.save(tx);

        eventProducer.publishFailed(new TransactionFailedEvent(
                tx.getTransactionId(), tx.getAccountId(), tx.getAmount(),
                reason, authenticatedUserId, Instant.now()
        ));
    }
}
