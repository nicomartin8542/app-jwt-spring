package com.nico.curso.springboot.app.springboot_crud.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class ValidationsUtils {

    public ResponseEntity<?> validationFields(BindingResult result) {

        Map<String, Object> errors = new HashMap<>();

        result.getFieldErrors().forEach((e) -> {
            errors.put(e.getField(), "El campo " + e.getField() + " " + e.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    public ResponseEntity<String> validationFields(BindingResult result, String message) {
        return ResponseEntity.badRequest().body(message);
    }

}
