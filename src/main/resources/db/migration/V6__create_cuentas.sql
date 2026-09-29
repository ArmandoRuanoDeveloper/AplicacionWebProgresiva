CREATE TABLE IF NOT EXISTS cuentas (
    id                  SERIAL PRIMARY KEY,
    numero_cuenta       VARCHAR(20)     NOT NULL,
    saldo               NUMERIC(14,2)   NOT NULL DEFAULT 0,
    estatus             VARCHAR(20)     NOT NULL DEFAULT 'ACTIVA',
    cliente_id          INTEGER         NOT NULL,
    fecha_creacion      TIMESTAMP       NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_cuentas_numero UNIQUE (numero_cuenta),
    CONSTRAINT fk_cuentas_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT chk_cuentas_saldo CHECK (saldo >= 0)
);

CREATE INDEX IF NOT EXISTS idx_cuentas_cliente_id ON cuentas(cliente_id);