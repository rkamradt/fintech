package com.fintech.transactionservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AccountBalanceUpdatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(AccountBalanceUpdatedConsumer.class);

    @KafkaListener(
            topics = "${kafka.topics.account-balance-updated}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onAccountBalanceUpdated(AccountBalanceUpdatedEvent event) {
        // Confirms that AccountService successfully applied the balance update for this transaction.
        // Used for audit/reconciliation — no further action required here.
        log.info("Confirmed account.balance.updated: accountId={}, transactionId={}, newBalance={}",
                event.accountId(), event.transactionId(), event.newBalance());
    }
}
