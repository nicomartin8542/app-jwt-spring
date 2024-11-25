package com.nico.curso.springboot.app.springboot_crud.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenJwtConfig {

    // Genero la clave secreta para poder validar el jwt
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    // Variables estaticas
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}
