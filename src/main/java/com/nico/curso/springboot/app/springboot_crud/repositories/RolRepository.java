package com.nico.curso.springboot.app.springboot_crud.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nico.curso.springboot.app.springboot_crud.model.entities.Role;

public interface RolRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
