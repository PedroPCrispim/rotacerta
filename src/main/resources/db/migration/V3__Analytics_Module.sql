CREATE TABLE analytics_route_summaries (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    route_id UUID NOT NULL,
    vehicle_id UUID NOT NULL,
    distance_optimized BIGINT,
    distance_original BIGINT,
    time_optimized BIGINT,
    time_original BIGINT,
    fuel_consumption_optimized DOUBLE PRECISION,
    fuel_cost_optimized DECIMAL(19, 2),
    points_count INTEGER,
    capacity_utilization DOUBLE PRECISION,
    status VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE analytics_daily_aggregators (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    reference_date DATE NOT NULL,
    km_saved DOUBLE PRECISION,
    time_saved BIGINT,
    cost_saved DECIMAL(19, 2),
    deliveries_count INTEGER,
    active_vehicles_count INTEGER,
    UNIQUE (tenant_id, reference_date)
);

CREATE TABLE analytics_events (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_analytics_route_tenant ON analytics_route_summaries(tenant_id, created_at);
CREATE INDEX idx_analytics_daily_tenant ON analytics_daily_aggregators(tenant_id, reference_date);
CREATE INDEX idx_analytics_events_tenant ON analytics_events(tenant_id, created_at);
