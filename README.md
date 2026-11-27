# Peachcar

A premium, Kenya-first vehicle marketplace built as a separate Peachcar implementation. The design takes broad inspiration from the public automotive retail experience at https://peachcars.co.ke/; it does not copy proprietary code, brand assets, testimonials or stock data.

## Stack

- Next.js App Router, React and TypeScript
- Java 21 / Spring Boot 3.5
- PostgreSQL with Flyway migrations
- Cloudinary for server-side image uploads and delivery

## Implemented

- Responsive public homepage with live catalogue data from PostgreSQL
- Public vehicle detail pages with gallery, description, KES price, phone and WhatsApp contacts
- Public inventory exposes only PUBLISHED vehicles
- Admin login backed by PostgreSQL administrator records and BCrypt password hashes
- Session-based admin authentication, session ID rotation, CSRF token cookie/header protection
- Admin inventory management: create, edit, delete, publish, unpublish and mark sold
- Multiple JPEG, PNG and WebP uploads per vehicle, with MIME/signature checks and 10 MB per-image limit
- Central dealership phone, WhatsApp and email settings
- Cloudinary asset deletion when a vehicle is deleted
- CI workflow for frontend production build and backend Maven verification

## Local setup

Requirements: Node.js 22+, Java 21, Maven 3.9+, PostgreSQL 15+.

1. Create a PostgreSQL database named `peachcar`.
2. Set backend environment variables:
   - `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/peachcar`
   - `DB_USERNAME` and `DB_PASSWORD`
   - `PEACHCAR_ADMIN_EMAIL`
   - `PEACHCAR_ADMIN_PASSWORD` (minimum 14 characters)
   - `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`
   - `SESSION_COOKIE_SECURE=false` for local HTTP; set `true` behind HTTPS in production.
3. Run the API from `backend/` with `mvn spring-boot:run`. Flyway creates the schema and the bootstrap creates the first admin if both admin variables are set.
4. Set `API_BASE_URL=http://localhost:8080` in the Next.js environment.
5. From the repository root run `npm install` and `npm run dev`.
6. Visit `http://localhost:3000/admin`.

Never commit credentials, database URLs containing passwords, or Cloudinary secrets. Use deployment secret/environment settings.

## Deployment notes

- Frontend: deploy the repository root to Vercel and set `API_BASE_URL` to the HTTPS Spring Boot API origin. The Next.js rewrite proxies browser API requests through the frontend origin.
- Backend: deploy the `backend/` directory as a Java service (build: `mvn -B clean package`; start: `java -jar target/peachcar-api-0.1.0.jar`).
- Database: set `SPRING_DATASOURCE_URL` to a JDBC PostgreSQL URL and provide DB credentials separately.
- Set `SESSION_COOKIE_SECURE=true`, `PEACHCAR_ADMIN_EMAIL`, `PEACHCAR_ADMIN_PASSWORD`, and all three Cloudinary credentials on the backend.
- Set `APP_ORIGIN` to the exact deployed frontend origin; use comma-separated exact origins if you support multiple Vercel preview domains.
- The initial admin is only inserted when its email does not already exist. Password rotation for an existing admin should be performed through a separately secured credential-management procedure.

## Routes

Public:
- `GET /api/public/vehicles`
- `GET /api/public/vehicles/{id}`
- `GET /api/public/settings`

Admin (authenticated session + CSRF for state-changing requests):
- `POST /api/admin/auth/login`
- `GET /api/admin/auth/me`
- `POST /api/admin/auth/logout`
- `GET/POST /api/admin/vehicles`
- `PUT/DELETE /api/admin/vehicles/{id}`
- `PATCH /api/admin/vehicles/{id}/status`
- `POST /api/admin/vehicles/{id}/images`
- `GET/PUT /api/admin/settings`

## Important operational notes

- Configure the Cloudinary credentials before enabling photo uploads.
- The admin bootstrap is environment-driven; no default username/password exists.
- The UI is not a substitute for operational controls such as database backups, alerting, login rate limiting, admin password reset, MFA, and production security review. Those should be completed before handling customer enquiries or live inventory at scale.
