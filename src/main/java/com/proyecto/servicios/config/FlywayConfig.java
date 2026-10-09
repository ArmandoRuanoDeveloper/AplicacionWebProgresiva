package com.proyecto.servicios.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class FlywayConfig {

    @Value("${spring.flyway.locations:classpath:db/migration}")
    private String[] locations;

    @Value("${spring.flyway.table:flyway_schema_history}")
    private String historyTable;

    @Value("${spring.flyway.schemas:public}")
    private String schema;

    
    @Bean(name = "flyway")
    public Flyway flyway(@Qualifier("sfDatasource") DataSource dataSource) {
        log.info("FLYWAY 1: Iniciando configuración. Schema: '{}'", schema);

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .table(historyTable)
                .schemas(schema)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .load();

        log.info("FLYWAY 2: Configuración completada. Iniciando migraciones.");

        try {
            flyway.migrate();
            log.info("FLYWAY 3: Migraciones completadas correctamente.");
        } catch (Exception e) {
            log.error("FLYWAY ERROR: Fallaron las migraciones.", e);
            throw e;
        }

        return flyway;
    }
}
