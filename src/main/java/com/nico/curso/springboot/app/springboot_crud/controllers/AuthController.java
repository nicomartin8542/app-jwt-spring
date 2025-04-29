package com.nico.curso.springboot.app.springboot_crud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nico.curso.springboot.app.springboot_crud.model.dto.token.TokenRequestDto;
import com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth.UserNewPassword;
import com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth.UserReSendEmail;
import com.nico.curso.springboot.app.springboot_crud.model.entities.User;
import com.nico.curso.springboot.app.springboot_crud.services.auth.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/get-user")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok().body(authService.getUserData());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.save(user));
    }

    // Eliminar la anotación @PreAuthorize para permitir acceso a todos los usuarios
    @PostMapping("/create-account")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        user.setAdmin(false);
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.save(user));
    }

    @PostMapping("/confirm-account")
    public ResponseEntity<?> confirmAccount(@Valid @RequestBody TokenRequestDto token) {
        return authService.confirmAccount(token.getToken()).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/resend-email")
    public ResponseEntity<?> resendEmailBycode(@Valid @RequestBody UserReSendEmail user) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.resendEmailBycode(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody UserReSendEmail user) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.forgotPassword(user));
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@Valid @RequestBody TokenRequestDto token) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.verifyToken(token));
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody UserNewPassword user, @PathVariable String token) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.resetPassword(user, token));
    }

}