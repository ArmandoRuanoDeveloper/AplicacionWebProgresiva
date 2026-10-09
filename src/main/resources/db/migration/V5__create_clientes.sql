CREATE TABLE IF NOT EXISTS clientes (
    id                    SERIAL PRIMARY KEY,
    nombre                VARCHAR(50)     NOT NULL,
    segundo_nombre        VARCHAR(50),
    apellido_paterno      VARCHAR(50)     NOT NULL,
    apellido_materno      VARCHAR(50)     NOT NULL,
    fecha_nacimiento      DATE            NOT NULL,
    curp                  CHAR(18)        NOT NULL,
    rfc                   VARCHAR(13)     NOT NULL,
    sexo                  VARCHAR(20)     NOT NULL,
    nacionalidad          VARCHAR(50)     NOT NULL,
    estado_civil          VARCHAR(30)     NOT NULL,
    correo                VARCHAR(100)    NOT NULL,
    telefono_movil        CHAR(10)        NOT NULL,
    telefono_alternativo  CHAR(10),
    ocupacion             VARCHAR(100)    NOT NULL,
    empresa               VARCHAR(100)    NOT NULL,
    ingreso_mensual       NUMERIC(12,2)   NOT NULL,
    domicilio_id          INTEGER         NOT NULL,
    activo                BOOLEAN         NOT NULL DEFAULT TRUE,
    fecha_creacion        TIMESTAMP       NOT NULL DEFAULT NOW(),
    fecha_actualizacion   TIMESTAMP       NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_clientes_curp UNIQUE (curp),
    CONSTRAINT uq_clientes_rfc UNIQUE (rfc),
    CONSTRAINT uq_clientes_correo UNIQUE (correo),
    CONSTRAINT fk_clientes_domicilio FOREIGN KEY (domicilio_id) REFERENCES domicilios(id),
    CONSTRAINT chk_clientes_ingreso CHECK (ingreso_mensual > 0)
);

CREATE INDEX IF NOT EXISTS idx_clientes_activo ON clientes(activo);
CREATE INDEX IF NOT EXISTS idx_clientes_fecha_creacion ON clientes(fecha_creacion);