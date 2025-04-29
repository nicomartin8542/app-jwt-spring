package com.nico.curso.springboot.app.springboot_crud.security.exceptions;

public class EntityExistException extends RuntimeException {

    public EntityExistException(String message) {
        super(message);
    }

}