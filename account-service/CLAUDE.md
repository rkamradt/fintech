# AccountService
Part of the **New Ecosystem** ecosystem.

## This service owns
Owns account lifecycle, account types, balance management, and account status for all financial accounts in the system

## Tech stack
Spring Boot

## API contracts — implement exactly as specified
- `POST /accounts` — Create new account with specified type (checking, savings, etc.) for a customer
- `GET /accounts/{accountId}` — Get account details including current balance and status
- `GET /accounts/customer/{customerId}` — List all accounts owned by a specific customer
- `PUT /accounts/{accountId}/status` — Update account status (active, suspended, closed) - manager only
- `PUT /accounts/{accountId}/balance` — Update account balance after transaction processing

## Event contracts — implement exactly as specified
- ▶ Produces `account.created` — Published when new account is created with accountId, customerId, accountType, initialBalance
- ▶ Produces `account.balance.updated` — Published when account balance changes with accountId, newBalance, previousBalance, transactionId
- ▶ Produces `account.status.changed` — Published when account status changes with accountId, newStatus, previousStatus, authorizedBy
- ◀ Consumes `transaction.completed` — Listens for completed transactions to update account balances accordingly

## Service dependencies
(none)

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

