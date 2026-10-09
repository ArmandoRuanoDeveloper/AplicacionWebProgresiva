CREATE TABLE IF NOT EXISTS verificaciones_faciales (
    id                  SERIAL PRIMARY KEY,
    usuario_id          INTEGER         NOT NULL,
    resultado           VARCHAR(10)     NOT NULL,
    score_similitud     NUMERIC(5,4),
    contexto            VARCHAR(50)     NOT NULL,
    referencia_id       INTEGER,
    fecha_verificacion  TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_verificacion_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT chk_verificacion_resultado CHECK (resultado IN ('EXITOSA', 'FALLIDA')),
    CONSTRAINT chk_verificacion_score CHECK (score_similitud IS NULL OR score_similitud BETWEEN 0 AND 1)
);

CREATE INDEX IF NOT EXISTS idx_verificaciones_usuario_id ON verificaciones_faciales(usuario_id);
CREATE INDEX IF NOT EXISTS idx_verificaciones_fecha ON verificaciones_faciales(fecha_verificacion);