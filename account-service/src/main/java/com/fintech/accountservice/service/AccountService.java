package com.fintech.accountservice.service;

import com.fintech.accountservice.dto.CreateAccountRequest;
import com.fintech.accountservice.dto.UpdateBalanceRequest;
import com.fintech.accountservice.dto.UpdateStatusRequest;
import com.fintech.accountservice.kafka.AccountEventProducer;
import com.fintech.accountservice.kafka.event.AccountBalanceUpdatedEvent;
import com.fintech.accountservice.kafka.event.AccountCreatedEvent;
import com.fintech.accountservice.kafka.event.AccountStatusChangedEvent;
import com.fintech.accountservice.model.Account;
import com.fintech.accountservice.model.AccountStatus;
import com.fintech.accountservice.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountEventProducer eventProducer;

    public AccountService(AccountRepository accountRepository, AccountEventProducer eventProducer) {
        this.accountRepository = accountRepository;
        this.eventProducer = eventProducer;
    }

    @Transactional
    public Account createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setCustomerId(request.customerId());
        account.setAccountType(request.accountType());
        account.setBalance(request.initialBalance() != null ? request.initialBalance() : BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);

        Account saved = accountRepository.save(account);

        eventProducer.publishAccountCreated(new AccountCreatedEvent(
                saved.getAccountId(),
                saved.getCustomerId(),
                saved.getAccountType(),
                saved.getBalance()
        ));

        return saved;
    }

    public Account getAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account not found: " + accountId));
    }

    public List<Account> getAccountsByCustomer(String customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    @Transactional
    public Account updateStatus(String accountId, UpdateStatusRequest request) {
        Account account = getAccount(accountId);
        AccountStatus previousStatus = account.getStatus();

        account.setStatus(request.status());
        Account saved = accountRepository.save(account);

        eventProducer.publishStatusChanged(new AccountStatusChangedEvent(
                saved.getAccountId(),
                saved.getStatus(),
                previousStatus,
                request.authorizedBy()
        ));

        return saved;
    }

    @Transactional
    public Account updateBalance(String accountId, UpdateBalanceRequest request) {
        Account account = getAccount(accountId);
        BigDecimal previousBalance = account.getBalance();

        account.setBalance(request.newBalance());
        Account saved = accountRepository.save(account);

        eventProducer.publishBalanceUpdated(new AccountBalanceUpdatedEvent(
                saved.getAccountId(),
                saved.getBalance(),
                previousBalance,
                request.transactionId()
        ));

        return saved;
    }

    @Transactional
    public void applyTransactionBalance(String accountId, BigDecimal newBalance, String transactionId) {
        accountRepository.findById(accountId).ifPresent(account -> {
            BigDecimal previousBalance = account.getBalance();
            account.setBalance(newBalance);
            accountRepository.save(account);

            eventProducer.publishBalanceUpdated(new AccountBalanceUpdatedEvent(
                    accountId,
                    newBalance,
                    previousBalance,
                    transactionId
            ));
        });
    }
}
