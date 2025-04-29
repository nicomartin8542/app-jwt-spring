package com.nico.curso.springboot.app.springboot_crud.services.user;

import java.util.List;
import java.util.Optional;

import com.nico.curso.springboot.app.springboot_crud.model.entities.User;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    boolean existsByUsername(String username);

}
