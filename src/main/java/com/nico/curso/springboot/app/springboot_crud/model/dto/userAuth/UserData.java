package com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth;

import java.util.List;

import com.nico.curso.springboot.app.springboot_crud.model.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {

    private String username;
    private String name;
    private List<Role> roles;
}
