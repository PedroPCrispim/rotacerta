-- Corrige a senha padrão do administrador seed para "admin"
UPDATE users
SET password = '$2a$10$cJTexUL/ZCsFm/QIouaHAeMFm9Mz6fR1DvXB7Gl3nW8yUfSzvk4ea'
WHERE email = 'admin@rotacerta.com';
