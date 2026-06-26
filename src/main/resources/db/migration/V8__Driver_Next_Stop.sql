ALTER TABLE drivers
    ADD COLUMN next_stop_address_id UUID;

ALTER TABLE drivers
    ADD CONSTRAINT fk_driver_next_stop_address
        FOREIGN KEY (next_stop_address_id) REFERENCES addresses(id);

CREATE INDEX idx_drivers_next_stop_address ON drivers(next_stop_address_id);
