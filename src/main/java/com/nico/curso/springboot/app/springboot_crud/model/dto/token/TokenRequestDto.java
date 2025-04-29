package com.nico.curso.springboot.app.springboot_crud.model.dto.token;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenRequestDto {

    @NotNull
    private String token;
}
