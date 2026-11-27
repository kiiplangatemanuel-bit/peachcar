# Peachcar

Premium Kenya-first automotive marketplace starter inspired by the information architecture of Peach Cars (https://peachcars.co.ke/), with an original Peachcar identity and implementation.

## Current frontend

- Responsive public homepage and mobile navigation
- Vehicle discovery with working make, body-style and text filters
- KES price presentation
- Vehicle cards with save interaction and WhatsApp enquiry CTA
- Buying journey, trust proposition and sell-your-car CTA

**Important:** Current inventory is illustrative sample content, not live stock. The WhatsApp contact is a placeholder. Do not publish it as production inventory until real listings and centrally managed contact settings are connected.

## Stack

- Next.js App Router, React and TypeScript
- Java Spring Boot REST API (next milestone)
- PostgreSQL and Flyway (next milestone)
- Cloudinary or S3-compatible object storage (next milestone)

## Run locally

```bash
npm install
npm run dev
```

Open http://localhost:3000.

## Reference and originality

The public information architecture was reviewed at https://peachcars.co.ke/ on 27 November 2026. Peachcar is a separate implementation; it does not reuse Peach Cars' proprietary code, brand assets, testimonials or vehicle inventory.

## Next milestones

1. Spring Boot API, PostgreSQL schema and Flyway migrations
2. Admin authentication and vehicle CRUD
3. Multi-image uploads to Cloudinary/S3
4. Central WhatsApp/phone settings used by all vehicle detail pages
5. Dynamic public inventory, detail pages, enquiries and advanced search
6. Automated tests, deployment configuration and production hardening
