package com.nico.curso.springboot.app.springboot_crud.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthoritiesJsonCreator {

    public SimpleGrantedAuthoritiesJsonCreator(@JsonProperty("authority") String role) {

    }
}
