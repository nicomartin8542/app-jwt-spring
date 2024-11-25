package com.nico.curso.springboot.app.springboot_crud.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nico.curso.springboot.app.springboot_crud.entities.Role;
import com.nico.curso.springboot.app.springboot_crud.entities.User;
import com.nico.curso.springboot.app.springboot_crud.repositories.RolRepository;
import com.nico.curso.springboot.app.springboot_crud.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = false)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        Optional<Role> rol = rolRepository.findByName("ROLE_USER");
        // List<Role> roles = rol.isPresent() ? List.of(rol.get()) : List.of();
        List<Role> roles = new ArrayList<>();
        rol.ifPresent(roles::add);

        // Si es admin, agrego el rol admin
        if (user.isAdmin()) {
            Optional<Role> rolAdmin = rolRepository.findByName("ROLE_ADMIN");
            rolAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        System.out.println(user);

        return userRepository.save(user);
    }

}
