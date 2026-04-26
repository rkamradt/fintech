# TransactionService
Part of the **New Ecosystem** ecosystem.

## This service owns
Owns all money movement operations, transaction history, and audit trails with authenticated user context

## Tech stack
Spring Boot

## API contracts — implement exactly as specified
- `POST /transactions/deposit` — Deposit money to account - requires teller+ role, captures authenticated user and optional customer ID
- `POST /transactions/withdraw` — Withdraw money from account - requires teller+ role, captures authenticated user and optional customer ID
- `POST /transactions/transfer` — Transfer money between accounts - requires teller+ role, captures authenticated user
- `GET /transactions/account/{accountId}` — Get transaction history for account - customer sees own, teller+ sees any
- `GET /transactions/{transactionId}` — Get specific transaction details including audit trail
- `GET /transactions/user/{userId}` — Get all transactions performed by a specific user - manager only

## Event contracts — implement exactly as specified
- ▶ Produces `transaction.initiated` — Published when transaction starts with transactionId, accountId, amount, type, authenticatedUserId, customerIdOnBehalf
- ▶ Produces `transaction.completed` — Published when transaction completes successfully with transactionId, accountId, amount, newBalance, authenticatedUserId
- ▶ Produces `transaction.failed` — Published when transaction fails with transactionId, accountId, amount, reason, authenticatedUserId
- ◀ Consumes `account.balance.updated` — Listens to confirm balance updates were applied successfully

## Service dependencies
- `account-service`

## Platform context
See @../ecosystem.json for the full ecosystem registry.
See @../CLAUDE.md for platform-wide architecture rules.

## Build & run
```bash
mvn spring-boot:run
mvn test
```

## Implementation notes
- Do not share a database with any other service
- All inter-service calls must go through the declared API or event contracts above
- Prefer async (Kafka) for cross-domain side effects; use sync API calls only for same-domain queries

