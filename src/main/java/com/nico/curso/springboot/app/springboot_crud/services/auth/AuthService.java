package com.nico.curso.springboot.app.springboot_crud.services.auth;

import com.nico.curso.springboot.app.springboot_crud.model.dto.token.TokenRequestDto;
import com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth.UserData;
import com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth.UserNewPassword;
import com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth.UserReSendEmail;
import com.nico.curso.springboot.app.springboot_crud.model.entities.User;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface AuthService {
    String save(User user);

    Optional<String> confirmAccount(String token);

    String resendEmailBycode(UserReSendEmail user);

    void logout(HttpServletResponse response);

    UserData getUserData();

    String forgotPassword(UserReSendEmail user);

    String resetPassword(UserNewPassword user, String token);

    String verifyToken(TokenRequestDto token);
}
