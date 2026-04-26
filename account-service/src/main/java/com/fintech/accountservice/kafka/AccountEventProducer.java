package com.fintech.accountservice.kafka;

import com.fintech.accountservice.kafka.event.AccountBalanceUpdatedEvent;
import com.fintech.accountservice.kafka.event.AccountCreatedEvent;
import com.fintech.accountservice.kafka.event.AccountStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AccountEventProducer {

    private static final Logger log = LoggerFactory.getLogger(AccountEventProducer.class);

    private static final String TOPIC_ACCOUNT_CREATED = "account.created";
    private static final String TOPIC_BALANCE_UPDATED = "account.balance.updated";
    private static final String TOPIC_STATUS_CHANGED = "account.status.changed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AccountEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishAccountCreated(AccountCreatedEvent event) {
        log.info("Publishing account.created for accountId={}", event.accountId());
        kafkaTemplate.send(TOPIC_ACCOUNT_CREATED, event.accountId(), event);
    }

    public void publishBalanceUpdated(AccountBalanceUpdatedEvent event) {
        log.info("Publishing account.balance.updated for accountId={}", event.accountId());
        kafkaTemplate.send(TOPIC_BALANCE_UPDATED, event.accountId(), event);
    }

    public void publishStatusChanged(AccountStatusChangedEvent event) {
        log.info("Publishing account.status.changed for accountId={}", event.accountId());
        kafkaTemplate.send(TOPIC_STATUS_CHANGED, event.accountId(), event);
    }
}
