CREATE TABLE IF NOT EXISTS domicilios (
    id                  SERIAL PRIMARY KEY,
    calle               VARCHAR(150)    NOT NULL,
    numero_exterior     VARCHAR(20)     NOT NULL,
    numero_interior     VARCHAR(20),
    colonia             VARCHAR(100)    NOT NULL,
    municipio           VARCHAR(100)    NOT NULL,
    estado              VARCHAR(100)    NOT NULL,
    codigo_postal       CHAR(5)         NOT NULL,
    pais                VARCHAR(100)    NOT NULL,
    fecha_creacion      TIMESTAMP       NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP       NOT NULL DEFAULT NOW()
);