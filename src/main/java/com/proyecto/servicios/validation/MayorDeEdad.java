package com.proyecto.servicios.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MayorDeEdadValidator.class)
public @interface MayorDeEdad {
    String message() default "El cliente debe ser mayor de edad";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}