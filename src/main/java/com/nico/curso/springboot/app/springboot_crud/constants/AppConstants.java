package com.nico.curso.springboot.app.springboot_crud.constants;

public final class AppConstants {
    private AppConstants() {
    } // Previene la instanciación

    public static final int TOKEN_EXPIRATION_MINUTES = 10; // 10 minutos
    public static final int SCHEDULE_TOKEN_CLEANUP = 5 * 60 * 1000; // 5 minutos
    public static final int TOKEN_LIMIT = 3;
    public static final int TOKEN_LIMIT_MINUTES = 2;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 20;

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String COOKIE_NAME = "token";
    public static final String MSG_ERROR_BAD_CREDENTIALS = "Usuario o contraseña incorrectos. Verifica y vuelve a intentar!";
    public static final String MSG_ERROR_USER_NOT_CONFIRMED = "El usuario no ha sido confirmado, por favor revisa tu correo";
    public static final String MSG_ERROR_USER_DISABLED = "El usuario esta deshabilitado";
}
