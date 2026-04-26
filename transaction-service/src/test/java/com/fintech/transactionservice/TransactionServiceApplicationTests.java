package com.fintech.transactionservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=localhost:19092",
        "services.account-service.url=http://localhost:18081",
        "services.user-service.url=http://localhost:18083",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
class TransactionServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
