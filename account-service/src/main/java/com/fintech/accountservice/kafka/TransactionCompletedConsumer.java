package com.fintech.accountservice.kafka;

import com.fintech.accountservice.kafka.event.TransactionCompletedEvent;
import com.fintech.accountservice.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionCompletedConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransactionCompletedConsumer.class);

    private final AccountService accountService;

    public TransactionCompletedConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

    @KafkaListener(topics = "transaction.completed", groupId = "account-service")
    public void onTransactionCompleted(TransactionCompletedEvent event) {
        log.info("Received transaction.completed for transactionId={} accountId={}",
                event.transactionId(), event.accountId());
        accountService.applyTransactionBalance(event.accountId(), event.newBalance(), event.transactionId());
    }
}
