-- Insertar los roles ROLE_ADMIN y ROLE_USER
INSERT INTO roles (name) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_USER')
ON CONFLICT (name) DO NOTHING; 