# UserService

Owns user identity, role assignments, and user lifecycle management for the banking system. Authentication is handled externally by Auth0 via the api-gateway.

## Build & run

```bash
mvn spring-boot:run
mvn test
```

## API

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/users` | Create new user (manager only) |
| `GET` | `/users/{userId}` | Get user profile and role |
| `PUT` | `/users/{userId}/role` | Update user role (manager only) |
| `PUT` | `/users/{userId}/status` | Activate/deactivate user (manager only) |
| `GET` | `/users/tellers` | List all tellers (manager only) |
| `GET` | `/users/customers` | List all customers (teller+) |

See `../spec.md` for full contracts.
