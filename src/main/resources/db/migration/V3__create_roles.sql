CREATE TABLE IF NOT EXISTS roles (
    id      SERIAL PRIMARY KEY,
    nombre  VARCHAR(20) NOT NULL,
    CONSTRAINT uq_roles_nombre UNIQUE (nombre)
);

INSERT INTO roles (nombre) VALUES ('ADMINISTRADOR'), ('CLIENTE');