package com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserReSendEmail {

    @NotNull
    private String username;
}
