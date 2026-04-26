# New Ecosystem — Platform Architecture Context

## Authoritative files
- @spec.md — human-readable living specification (source of truth for all AI operations)
- @ecosystem.json — machine-readable service registry (APIs, event contracts, dependencies)

## Services in this platform
- **AccountService** (`account-service`) — Owns account lifecycle, account types, balance management, and account status for all financial accounts in the system
- **TransactionService** (`transaction-service`) — Owns all money movement operations, transaction history, and audit trails with authenticated user context
- **UserService** (`user-service`) — Owns user identity, authentication, role assignments, and user lifecycle management for the banking system

## Architecture principles
- Each service owns exactly one bounded context — no shared databases
- Cross-domain communication via Kafka events; same-domain via direct API calls
- spec.md is the contract — code must match spec, not the other way around

## Three AI operations (see spec.md §AI Operation Protocol for full prompts)

### Forward — scaffold/implement a service
Read @spec.md, find the service section by its ID, implement exactly what is declared.

### Reverse — reconstruct spec from existing code
Walk the codebase, identify endpoints/events/dependencies, write them back into @spec.md
in the standard format. Preserve Implementation Notes already present.

### Delta — implement spec changes
```bash
git diff HEAD~1 -- spec.md > spec.diff
claude "Read spec.diff, implement only the changed service sections."
```

## Adding a new service
1. Architect it in ArchitectAI → push updated spec.md + ecosystem.json
2. In the service repo: `claude "Scaffold this service per @../rkamradt-platform/spec.md#{service-id}`
3. Place the generated service CLAUDE.md at the repo root

