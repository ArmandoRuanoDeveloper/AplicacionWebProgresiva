package com.proyecto.servicios.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    public Module recorteDeTextos() {
        SimpleModule modulo = new SimpleModule();
        modulo.addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
                String valor = p.getValueAsString();
                if (valor == null || "password".equals(p.currentName())) {
                    return valor;
                }
                valor = valor.strip();
                return valor.isEmpty() ? null : valor;
            }
        });
        return modulo;
    }
}