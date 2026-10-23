package com.salesianostriana.dam.talleres.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FechaFinPosteriorValidator.class)
@Documented
public @interface FechaFinPosterior {

    String message() default "La fecha de fin debe ser posterior a la fecha de inicio";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
