CREATE TABLE vehicles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  make VARCHAR(80) NOT NULL,
  model VARCHAR(100) NOT NULL,
  year INTEGER NOT NULL CHECK (year BETWEEN 1950 AND 2100),
  price_kes NUMERIC(14,2) NOT NULL CHECK (price_kes >= 0),
  mileage_km INTEGER CHECK (mileage_km >= 0),
  fuel VARCHAR(30),
  transmission VARCHAR(30),
  body_type VARCHAR(40),
  exterior_colour VARCHAR(40),
  description TEXT,
  status VARCHAR(20) NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT','PUBLISHED','SOLD')),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_vehicles_status_created ON vehicles(status, created_at DESC);

CREATE TABLE dealership_settings (
  id BIGINT PRIMARY KEY CHECK (id = 1),
  dealership_name VARCHAR(120) NOT NULL DEFAULT 'Peachcar',
  phone_number VARCHAR(30),
  whatsapp_number VARCHAR(30),
  contact_email VARCHAR(180),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
INSERT INTO dealership_settings (id, dealership_name) VALUES (1, 'Peachcar');
