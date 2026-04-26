# TransactionService

Owns all money movement operations, transaction history, and audit trails with authenticated user context.

## Build & run

```bash
mvn spring-boot:run
mvn test
```

## API

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/transactions/deposit` | Deposit money to account (teller+) |
| `POST` | `/transactions/withdraw` | Withdraw money from account (teller+) |
| `POST` | `/transactions/transfer` | Transfer between accounts (teller+) |
| `GET` | `/transactions/account/{accountId}` | Transaction history for account |
| `GET` | `/transactions/{transactionId}` | Get specific transaction |
| `GET` | `/transactions/user/{userId}` | Transactions by user (manager only) |

See `../spec.md` for full contracts.
