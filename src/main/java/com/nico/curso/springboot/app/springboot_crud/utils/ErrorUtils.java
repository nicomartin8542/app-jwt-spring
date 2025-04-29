package com.nico.curso.springboot.app.springboot_crud.utils;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ErrorUtils {

    public static Map<String, String> convertJson(String key, String message) {
        return Map.of(key, message);
    }

}
