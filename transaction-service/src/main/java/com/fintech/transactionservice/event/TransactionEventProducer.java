package com.fintech.transactionservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventProducer {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.transaction-initiated}")
    private String initiatedTopic;

    @Value("${kafka.topics.transaction-completed}")
    private String completedTopic;

    @Value("${kafka.topics.transaction-failed}")
    private String failedTopic;

    public TransactionEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishInitiated(TransactionInitiatedEvent event) {
        log.info("Publishing transaction.initiated for transactionId={}", event.transactionId());
        kafkaTemplate.send(initiatedTopic, event.transactionId(), event);
    }

    public void publishCompleted(TransactionCompletedEvent event) {
        log.info("Publishing transaction.completed for transactionId={}", event.transactionId());
        kafkaTemplate.send(completedTopic, event.transactionId(), event);
    }

    public void publishFailed(TransactionFailedEvent event) {
        log.info("Publishing transaction.failed for transactionId={}", event.transactionId());
        kafkaTemplate.send(failedTopic, event.transactionId(), event);
    }
}
