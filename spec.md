# New Ecosystem — System Specification
<!-- spec-version: 1.0.0 -->
<!-- generated: 2026-04-25 -->
<!-- ecosystem-hash: W3siaWQiOiJh -->
<!-- DO NOT remove the HTML comment markers — they are machine-readable section delimiters -->

## System Overview

**New Ecosystem** is a platform consisting of 3 microservices.

> _Edit this section to describe the system's overall purpose, the domain it operates in,
> and the key business capabilities it provides._

### Services at a glance

- **AccountService** (`account-service`) — Owns account lifecycle, account types, balance management, and account status for all financial accounts in the system
- **TransactionService** (`transaction-service`) — Owns all money movement operations, transaction history, and audit trails with authenticated user context
- **UserService** (`user-service`) — Owns user identity, authentication, role assignments, and user lifecycle management for the banking system

### Architecture principles

- Each service owns exactly one bounded context — no shared databases
- Cross-domain communication via Kafka events; same-domain via direct REST/gRPC
- API contracts are the only stable public interface of a service
- Prefer async event-driven patterns for cross-domain side effects

---

<!-- service-start: account-service -->
## Service: AccountService

**ID:** `account-service`
**Tech:** Spring Boot
**Purpose:** Owns account lifecycle, account types, balance management, and account status for all financial accounts in the system

### API Surface

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/accounts` | Create new account with specified type (checking, savings, etc.) for a customer |
| `GET` | `/accounts/{accountId}` | Get account details including current balance and status |
| `GET` | `/accounts/customer/{customerId}` | List all accounts owned by a specific customer |
| `PUT` | `/accounts/{accountId}/status` | Update account status (active, suspended, closed) - manager only |
| `PUT` | `/accounts/{accountId}/balance` | Update account balance after transaction processing |

### Event Contracts

| Direction | Topic | Description |
|-----------|-------|-------------|
| **Produces** | `account.created` | Published when new account is created with accountId, customerId, accountType, initialBalance |
| **Produces** | `account.balance.updated` | Published when account balance changes with accountId, newBalance, previousBalance, transactionId |
| **Produces** | `account.status.changed` | Published when account status changes with accountId, newStatus, previousStatus, authorizedBy |
| **Consumes** | `transaction.completed` | Listens for completed transactions to update account balances accordingly |

### Service Dependencies

_No dependencies._

### Data Ownership

This service is the sole owner of its data store. No other service may read or write its database directly.

### Implementation Notes

_Add service-specific constraints, patterns, and architectural decisions here._

<!-- service-end: account-service -->

---

<!-- service-start: transaction-service -->
## Service: TransactionService

**ID:** `transaction-service`
**Tech:** Spring Boot
**Purpose:** Owns all money movement operations, transaction history, and audit trails with authenticated user context

### API Surface

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/transactions/deposit` | Deposit money to account - requires teller+ role, captures authenticated user and optional customer ID |
| `POST` | `/transactions/withdraw` | Withdraw money from account - requires teller+ role, captures authenticated user and optional customer ID |
| `POST` | `/transactions/transfer` | Transfer money between accounts - requires teller+ role, captures authenticated user |
| `GET` | `/transactions/account/{accountId}` | Get transaction history for account - customer sees own, teller+ sees any |
| `GET` | `/transactions/{transactionId}` | Get specific transaction details including audit trail |
| `GET` | `/transactions/user/{userId}` | Get all transactions performed by a specific user - manager only |

### Event Contracts

| Direction | Topic | Description |
|-----------|-------|-------------|
| **Produces** | `transaction.initiated` | Published when transaction starts with transactionId, accountId, amount, type, authenticatedUserId, customerIdOnBehalf |
| **Produces** | `transaction.completed` | Published when transaction completes successfully with transactionId, accountId, amount, newBalance, authenticatedUserId |
| **Produces** | `transaction.failed` | Published when transaction fails with transactionId, accountId, amount, reason, authenticatedUserId |
| **Consumes** | `account.balance.updated` | Listens to confirm balance updates were applied successfully |

### Service Dependencies

| Service | 
|---------|
| `account-service` |

### Data Ownership

This service is the sole owner of its data store. No other service may read or write its database directly.

### Implementation Notes

_Add service-specific constraints, patterns, and architectural decisions here._

<!-- service-end: transaction-service -->

---

<!-- service-start: user-service -->
## Service: UserService

**ID:** `user-service`
**Tech:** Spring Boot
**Purpose:** Owns user identity, authentication, role assignments, and user lifecycle management for the banking system

### API Surface

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/users` | Create new user with specified role (customer, teller, manager) - manager only |
| `GET` | `/users/{userId}` | Get user profile and role information |
| `PUT` | `/users/{userId}/role` | Update user role assignment - manager only |
| `PUT` | `/users/{userId}/status` | Activate/deactivate user account - manager only |
| `GET` | `/users/tellers` | List all teller users for manager oversight |
| `GET` | `/users/customers` | List all customer users - teller+ role required |

### Event Contracts

| Direction | Topic | Description |
|-----------|-------|-------------|
| **Produces** | `user.created` | Published when new user is created with userId, role, createdBy, customerProfile if applicable |
| **Produces** | `user.role.changed` | Published when user role is updated with userId, newRole, previousRole, changedBy |
| **Produces** | `user.status.changed` | Published when user is activated/deactivated with userId, newStatus, changedBy |

### Service Dependencies

_No dependencies._

### Data Ownership

This service is the sole owner of its data store. No other service may read or write its database directly.

### Implementation Notes

Authentication is handled externally by Auth0 via the api-gateway. All requests arrive with an `X-User-ID` header (Auth0 sub mapped to internal UUID) injected by the gateway. The service trusts this header to identify the caller and looks up their role from its own database to enforce authorization. No passwords are stored; user records exist solely for profile and role metadata.

<!-- service-end: user-service -->

---

## Changelog

| Date | Change | Author |
|------|--------|--------|
| 2026-04-25 | Initial specification generated by ArchitectAI | ArchitectAI |
| 2026-04-25 | Removed /auth/login and /auth/validate; Auth0 via api-gateway is the auth provider; services trust X-User-ID header | Randy Sr |

---

## AI Operation Protocol

This document is the authoritative specification. Three AI operations are defined:

### 1. Forward — Implement from spec
```
claude "Read spec.md. For each service section, scaffold or implement the service
according to its API surface, event contracts, and dependencies. Do not deviate
from the contracts defined here."
```

### 2. Reverse — Reconstruct spec from code
```
claude "Examine this codebase. Identify all services, their REST/gRPC endpoints,
Kafka producers and consumers, and inter-service dependencies. Produce a
spec.md in the standard format (service-start/service-end markers, API table,
event table, dependency table). Preserve any existing Implementation Notes."
```

### 3. Delta — Implement spec changes
```bash
# Get the diff of what changed in the spec
git diff HEAD~1 -- spec.md > spec.diff

# Tell Claude Code to implement the delta
claude "Read spec.diff. For each changed service section, determine what code
changes are required (new endpoints, changed event schemas, new dependencies,
removed operations) and implement only those changes. Do not touch code for
unchanged services."
```

