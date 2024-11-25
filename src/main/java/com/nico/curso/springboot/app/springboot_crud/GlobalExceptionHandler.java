package com.nico.curso.springboot.app.springboot_crud;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.JsonMappingException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Excepcion para tipo de datos incorrectos en request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Error en el formato de los datos enviados.");

        Throwable cause = ex.getCause();
        if (cause instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) cause;

            // Obtener detalles de los campos problemáticos
            StringBuilder camposConError = new StringBuilder();
            jsonMappingException.getPath().forEach(ref -> {
                camposConError.append(ref.getFieldName()).append(" ");
            });

            errorResponse.put("detalles", "Error en los campos: " + camposConError.toString().trim());
        } else {
            errorResponse.put("detalles", "El formato de los datos enviados es incorrecto o no se puede convertir.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> methodNoSuported(HttpRequestMethodNotSupportedException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> noResourceFound(NoResourceFoundException ex) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> sQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
