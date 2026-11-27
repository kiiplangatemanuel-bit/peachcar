CREATE TABLE admin_users (
 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 email VARCHAR(190) NOT NULL UNIQUE,
 password_hash VARCHAR(100) NOT NULL,
 enabled BOOLEAN NOT NULL DEFAULT TRUE,
 created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE TABLE vehicle_images (
 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
 vehicle_id UUID NOT NULL REFERENCES vehicles(id) ON DELETE CASCADE,
 storage_public_id VARCHAR(255) NOT NULL,
 image_url TEXT NOT NULL,
 alt_text VARCHAR(240),
 sort_order INTEGER NOT NULL DEFAULT 0,
 created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_vehicle_images_vehicle_order ON vehicle_images(vehicle_id, sort_order);
