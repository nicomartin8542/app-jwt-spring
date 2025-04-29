package com.nico.curso.springboot.app.springboot_crud.valid.isRequired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = RequiredValidation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface IsRequired {

    String message() default "es requerido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
