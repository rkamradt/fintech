# AccountService

Owns account lifecycle, account types, balance management, and account status for all financial accounts in the system.

## Build & run

```bash
mvn spring-boot:run
mvn test
```

## API

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/accounts` | Create new account |
| `GET` | `/accounts/{accountId}` | Get account details |
| `GET` | `/accounts/customer/{customerId}` | List accounts by customer |
| `PUT` | `/accounts/{accountId}/status` | Update account status (manager only) |
| `PUT` | `/accounts/{accountId}/balance` | Update account balance |

See `../spec.md` for full contracts.
