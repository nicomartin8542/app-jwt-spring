package com.nico.curso.springboot.app.springboot_crud.valid.isRequired;

import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RequiredValidation implements ConstraintValidator<IsRequired, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // return (value != null && !value.isEmpty() && !value.trim().isEmpty());
        return StringUtils.hasText(value);
    }

}
