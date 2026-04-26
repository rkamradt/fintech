package com.fintech.transactionservice.repository;

import com.fintech.transactionservice.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByAccountIdOrderByCreatedAtDesc(String accountId);

    List<Transaction> findByAuthenticatedUserIdOrderByCreatedAtDesc(String authenticatedUserId);

    List<Transaction> findByAccountIdAndCustomerIdOnBehalfOrderByCreatedAtDesc(
            String accountId, String customerIdOnBehalf);
}
