package com.nico.curso.springboot.app.springboot_crud.services.token;

import java.util.Optional;

import com.nico.curso.springboot.app.springboot_crud.model.entities.Token;
import com.nico.curso.springboot.app.springboot_crud.model.entities.User;

public interface TokenService {

    void cleanExpiredTokens();

    String saveToken(User user);

    Optional<Token> verifyCode(String token);
}
