-- Keep the Spring Boot JDBC service as the only trusted data access path.
-- Supabase Data API roles must not read or mutate operational tables directly.
REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public FROM anon, authenticated, PUBLIC;
REVOKE ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public FROM anon, authenticated, PUBLIC;

ALTER TABLE public.flyway_schema_history ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.vehicles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.dealership_settings ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.admin_users ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.vehicle_images ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.customer_accounts ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.customer_wishlists ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.finance_enquiries ENABLE ROW LEVEL SECURITY;

-- Prevent newly-created public tables from inheriting broad Data API grants
-- for objects created by the Flyway migration role.
ALTER DEFAULT PRIVILEGES IN SCHEMA public
  REVOKE ALL ON TABLES FROM anon, authenticated, PUBLIC;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
  REVOKE ALL ON SEQUENCES FROM anon, authenticated, PUBLIC;
