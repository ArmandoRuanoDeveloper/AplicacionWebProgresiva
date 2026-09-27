CREATE TABLE IF NOT EXISTS productos (
    id                    SERIAL PRIMARY KEY,
    id_servicio           INTEGER         NOT NULL,
    id_producto           INTEGER         NOT NULL,
    servicio              VARCHAR(256),
    producto              VARCHAR(256),
    id_cat_tipo_servicio  INTEGER,
    tipo_front            INTEGER,
    precio                NUMERIC(15,2),
    tipo_referencia       VARCHAR(3),
    legend                TEXT,
    fecha_creacion        TIMESTAMP       NOT NULL DEFAULT NOW(),
    fecha_actualizacion   TIMESTAMP       NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_productos UNIQUE (id_servicio, id_producto)
);