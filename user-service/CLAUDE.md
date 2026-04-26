# UserService
Part of the **New Ecosystem** ecosystem.

## This service owns
Owns user identity, authentication, role assignments, and user lifecycle management for the banking system

## Tech stack
Spring Boot

## API contracts — implement exactly as specified
- `POST /users` — Create new user with specified role (customer, teller, manager) - manager only
- `GET /users/{userId}` — Get user profile and role information
- `PUT /users/{userId}/role` — Update user role assignment - manager only
- `PUT /users/{userId}/status` — Activate/deactivate user account - manager only
- `POST /auth/login` — Authenticate user and return JWT token with role claims
- `POST /auth/validate` — Validate JWT token and return user context for other services
- `GET /users/tellers` — List all teller users for manager oversight
- `GET /users/customers` — List all customer users - teller+ role required

## Event contracts — implement exactly as specified
- ▶ Produces `user.created` — Published when new user is created with userId, role, createdBy, customerProfile if applicable
- ▶ Produces `user.role.changed` — Published when user role is updated with userId, newRole, previousRole, changedBy
- ▶ Produces `user.status.changed` — Published when user is activated/deactivated with userId, newStatus, changedBy
- ▶ Produces `user.authenticated` — Published on successful login with userId, role, loginTimestamp for audit trails

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

