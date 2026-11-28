ALTER TABLE vehicles
  ADD COLUMN listing_type VARCHAR(24) NOT NULL DEFAULT 'LOCALLY_USED',
  ADD COLUMN duty_status VARCHAR(24) NOT NULL DEFAULT 'DUTY_PAID',
  ADD COLUMN inspection_status VARCHAR(24) NOT NULL DEFAULT 'NOT_INSPECTED',
  ADD COLUMN inspection_report TEXT,
  ADD COLUMN inspection_date DATE,
  ADD COLUMN accident_history VARCHAR(24) NOT NULL DEFAULT 'UNKNOWN';

ALTER TABLE vehicles
  ADD CONSTRAINT chk_vehicle_listing_type CHECK (listing_type IN ('FRESH_IMPORT','LOCALLY_USED')),
  ADD CONSTRAINT chk_vehicle_duty_status CHECK (duty_status IN ('DUTY_PAID','IN_BOND','DUTY_PENDING')),
  ADD CONSTRAINT chk_vehicle_inspection_status CHECK (inspection_status IN ('NOT_INSPECTED','INSPECTED')),
  ADD CONSTRAINT chk_vehicle_accident_history CHECK (accident_history IN ('UNKNOWN','NO_ACCIDENT_REPORTED','ACCIDENT_REPORTED'));

CREATE INDEX idx_vehicles_marketplace_filters
  ON vehicles(status, listing_type, duty_status, inspection_status, accident_history, created_at DESC);

CREATE TABLE customer_accounts (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  full_name VARCHAR(120) NOT NULL,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE customer_wishlists (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  customer_id UUID NOT NULL REFERENCES customer_accounts(id) ON DELETE CASCADE,
  vehicle_id UUID NOT NULL REFERENCES vehicles(id) ON DELETE CASCADE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_customer_wishlist_vehicle UNIQUE(customer_id, vehicle_id)
);
CREATE INDEX idx_customer_wishlists_customer_created ON customer_wishlists(customer_id, created_at DESC);

CREATE TABLE finance_enquiries (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  vehicle_id UUID REFERENCES vehicles(id) ON DELETE SET NULL,
  customer_id UUID REFERENCES customer_accounts(id) ON DELETE SET NULL,
  full_name VARCHAR(120) NOT NULL,
  email VARCHAR(190) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  monthly_income NUMERIC(14,2) NOT NULL CHECK (monthly_income >= 0),
  deposit_kes NUMERIC(14,2) NOT NULL CHECK (deposit_kes >= 0),
  preferred_term_months INTEGER NOT NULL CHECK (preferred_term_months BETWEEN 6 AND 84),
  message TEXT,
  status VARCHAR(20) NOT NULL DEFAULT 'NEW' CHECK (status IN ('NEW','IN_REVIEW','CONTACTED','CLOSED')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_finance_enquiries_status_created ON finance_enquiries(status, created_at DESC);
