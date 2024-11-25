package com.nico.curso.springboot.app.springboot_crud.valid.ExistByUserName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistByUserNameValidation.class)
@Target(value = { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistByUserName {

    String message() default "no puede estar repetido!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
