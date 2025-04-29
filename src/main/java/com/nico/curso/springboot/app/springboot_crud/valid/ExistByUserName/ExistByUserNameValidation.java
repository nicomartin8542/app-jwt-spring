package com.nico.curso.springboot.app.springboot_crud.valid.ExistByUserName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nico.curso.springboot.app.springboot_crud.services.user.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistByUserNameValidation implements ConstraintValidator<ExistByUserName, String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            return !userService.existsByUsername(value);
        } catch (Exception err) {
            return true;
        }
    }

}
