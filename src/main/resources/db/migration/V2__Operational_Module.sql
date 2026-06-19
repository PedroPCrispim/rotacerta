CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(20) NOT NULL,
    complement VARCHAR(100),
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(50) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_address_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE vehicles (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL,
    plate VARCHAR(20) NOT NULL,
    model VARCHAR(100) NOT NULL,
    capacity DOUBLE PRECISION NOT NULL,
    fuel_type VARCHAR(50) NOT NULL,
    avg_consumption DOUBLE PRECISION NOT NULL,
    cost_per_km DECIMAL(19, 2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vehicle_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE delivery_points (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL,
    address_id UUID NOT NULL,
    description VARCHAR(255),
    contact_name VARCHAR(255),
    priority INTEGER DEFAULT 3,
    start_time TIME,
    end_time TIME,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_delivery_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_delivery_address FOREIGN KEY (address_id) REFERENCES addresses(id)
);

CREATE INDEX idx_addresses_company ON addresses(company_id);
CREATE INDEX idx_vehicles_company ON vehicles(company_id);
CREATE INDEX idx_delivery_points_company ON delivery_points(company_id);
