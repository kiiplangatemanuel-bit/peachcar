# Peachcar API

Spring Boot 3.5 / Java 21 REST API.

## Local requirements

- Java 21
- Maven 3.9+
- PostgreSQL 15+

Set `DATABASE_URL`, `DB_USERNAME`, and `DB_PASSWORD`, then run:

```bash
mvn spring-boot:run
```

Flyway creates the initial schema. Hibernate validates it at startup.

## Public endpoints

- `GET /api/public/vehicles` — published inventory only
- `GET /api/public/settings` — centrally managed dealership contacts

Admin write endpoints are intentionally not exposed yet. They must be protected by admin authentication/authorization before enabling create, update, publish, sold-state, contact-setting or image-upload operations.
