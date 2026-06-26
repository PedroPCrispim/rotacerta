ALTER TABLE users
    ADD COLUMN can_view_operational BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN can_view_financial BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN can_view_fleet BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE users
SET can_view_operational = TRUE,
    can_view_financial = TRUE,
    can_view_fleet = TRUE
WHERE role = 'ADMIN';
