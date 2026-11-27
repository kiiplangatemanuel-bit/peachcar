# Peachcar API

Java 21 / Spring Boot 3.5 REST API backed by PostgreSQL and Flyway.

## Local requirements

- Java 21
- Maven 3.9+
- PostgreSQL 15+
- Cloudinary account for vehicle image uploads

Set `SPRING_DATASOURCE_URL` (JDBC URL), `DB_USERNAME`, `DB_PASSWORD`, `PEACHCAR_ADMIN_EMAIL`, `PEACHCAR_ADMIN_PASSWORD` (minimum 14 characters), and Cloudinary credentials. Then run:

```bash
mvn spring-boot:run
```

Flyway creates the schema. Hibernate validates the schema at startup. The initial administrator is created only when both bootstrap environment variables are supplied and the email does not already exist.

## Public endpoints

- `GET /api/public/vehicles` — published inventory only, including image URLs
- `GET /api/public/vehicles/{id}` — published vehicle detail only
- `GET /api/public/settings` — central dealership contacts
- `GET /api/csrf` — CSRF token for browser login

## Admin endpoints

All `/api/admin/**` routes require an authenticated ADMIN session except `POST /api/admin/auth/login`. Unsafe browser requests require the CSRF token in `X-XSRF-TOKEN`.

- Authentication: login, current session, logout
- Vehicle CRUD and status transitions
- Multi-image upload to Cloudinary
- Central phone, WhatsApp and email settings

## Upload controls

JPEG, PNG and WebP only; file signatures are checked; 10 MB maximum per file and up to 8 images per upload request. Cloudinary API secrets remain on the server. Configure `SESSION_COOKIE_SECURE=true` in HTTPS production deployments.

Production readiness still requires operational login throttling, MFA, credential reset/rotation workflows, backups, monitoring and a security review.
