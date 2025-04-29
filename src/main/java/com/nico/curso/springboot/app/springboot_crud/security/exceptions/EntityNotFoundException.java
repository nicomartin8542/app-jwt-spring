package com.nico.curso.springboot.app.springboot_crud.security.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
