package com.proyecto.servicios.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MayorDeEdadValidator implements ConstraintValidator<MayorDeEdad, LocalDate> {

    private static final int EDAD_MINIMA = 18;

    @Override
    public boolean isValid(LocalDate fechaNacimiento, ConstraintValidatorContext context) {
        if (fechaNacimiento == null) {
            return true; // el @NotNull ya se encarga de este caso, no se pisan validaciones
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            return false; // cubre también "no puede ser una fecha futura"
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= EDAD_MINIMA;
    }
}