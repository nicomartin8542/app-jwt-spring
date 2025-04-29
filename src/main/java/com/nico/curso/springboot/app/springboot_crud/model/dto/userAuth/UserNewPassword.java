package com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserNewPassword {

    @NotNull
    private String password;
    @NotNull
    private String passwordConfirmation;

}
