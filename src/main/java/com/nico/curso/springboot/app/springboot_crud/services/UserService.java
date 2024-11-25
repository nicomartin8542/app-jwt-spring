package com.nico.curso.springboot.app.springboot_crud.services;

import java.util.List;
import java.util.Optional;

import com.nico.curso.springboot.app.springboot_crud.entities.User;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    boolean existsByUsername(String username);

}
