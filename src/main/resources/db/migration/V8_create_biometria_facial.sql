CREATE TABLE IF NOT EXISTS biometria_facial (
    id                  SERIAL PRIMARY KEY,
    usuario_id          INTEGER         NOT NULL,
    plantilla_facial    TEXT            NOT NULL,
    proveedor           VARCHAR(50)     NOT NULL,
    activo              BOOLEAN         NOT NULL DEFAULT TRUE,
    fecha_registro      TIMESTAMP       NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_biometria_usuario UNIQUE (usuario_id),
    CONSTRAINT fk_biometria_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);