-- V4__Seed_Initial_Admin.sql
-- Cria uma empresa padrão
INSERT INTO companies (id, name, cnpj, active, created_at)
VALUES ('00000000-0000-0000-0000-000000000001', 'RotaCerta Matriz', '00.000.000/0001-01', true, CURRENT_TIMESTAMP);

-- Cria o usuário administrador inicial
-- Email: admin@rotacerta.com
-- Senha: admin (criptografada em BCrypt)
INSERT INTO users (id, company_id, name, email, password, role, created_at)
VALUES (
    '00000000-0000-0000-0000-000000000002', 
    '00000000-0000-0000-0000-000000000001', 
    'Administrador RotaCerta', 
    'admin@rotacerta.com', 
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.7uqqQ3a', 
    'ADMIN', 
    CURRENT_TIMESTAMP
);
