CREATE TABLE financial_settings (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL UNIQUE,
    region VARCHAR(120) NOT NULL,
    gasoline_price DECIMAL(10, 2) NOT NULL,
    ethanol_price DECIMAL(10, 2) NOT NULL,
    diesel_price DECIMAL(10, 2) NOT NULL,
    fixed_cost_per_vehicle DECIMAL(12, 2) NOT NULL,
    maintenance_reserve DECIMAL(12, 2) NOT NULL,
    toll_cost DECIMAL(12, 2) NOT NULL,
    driver_daily_cost DECIMAL(12, 2) NOT NULL,
    target_savings DECIMAL(12, 2) NOT NULL,
    max_cost_per_delivery DECIMAL(12, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_financial_settings_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE drivers (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(30) NOT NULL,
    license_number VARCHAR(50) NOT NULL,
    status VARCHAR(40) NOT NULL,
    assigned_vehicle_id UUID,
    current_route_label VARCHAR(120),
    route_execution_status VARCHAR(60),
    check_in_required BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_driver_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_driver_vehicle FOREIGN KEY (assigned_vehicle_id) REFERENCES vehicles(id)
);

CREATE INDEX idx_financial_settings_company ON financial_settings(company_id);
CREATE INDEX idx_drivers_company ON drivers(company_id);
CREATE INDEX idx_drivers_vehicle ON drivers(assigned_vehicle_id);

INSERT INTO financial_settings (
    id, company_id, region, gasoline_price, ethanol_price, diesel_price, fixed_cost_per_vehicle,
    maintenance_reserve, toll_cost, driver_daily_cost, target_savings, max_cost_per_delivery
) VALUES (
    '00000000-0000-0000-0000-000000000010',
    '00000000-0000-0000-0000-000000000001',
    'Sao Paulo',
    6.19,
    4.09,
    6.02,
    1800.00,
    650.00,
    280.00,
    180.00,
    4500.00,
    22.00
);

INSERT INTO drivers (
    id, company_id, name, email, phone, license_number, status,
    current_route_label, route_execution_status, check_in_required
) VALUES
(
    '00000000-0000-0000-0000-000000000011',
    '00000000-0000-0000-0000-000000000001',
    'Carlos Silva',
    'carlos.silva@rotacerta.com',
    '(11) 99999-1001',
    'CNH-001',
    'Ativo no dia',
    'Rota 01 / Base Centro',
    'Aguardando saida',
    true
),
(
    '00000000-0000-0000-0000-000000000012',
    '00000000-0000-0000-0000-000000000001',
    'Marina Souza',
    'marina.souza@rotacerta.com',
    '(11) 99999-1002',
    'CNH-002',
    'Em rota',
    'Rota 02 / Zona Sul',
    'Em rota',
    true
);
