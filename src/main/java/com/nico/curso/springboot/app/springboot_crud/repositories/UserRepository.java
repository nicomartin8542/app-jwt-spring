package com.nico.curso.springboot.app.springboot_crud.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nico.curso.springboot.app.springboot_crud.model.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    List<User> findAllByConfirmFalse();

}
