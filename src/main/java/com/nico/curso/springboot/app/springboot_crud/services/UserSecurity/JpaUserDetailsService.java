package com.nico.curso.springboot.app.springboot_crud.services.UserSecurity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nico.curso.springboot.app.springboot_crud.constants.AppConstants;
import com.nico.curso.springboot.app.springboot_crud.model.entities.User;
import com.nico.curso.springboot.app.springboot_crud.repositories.UserRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new BadCredentialsException(AppConstants.MSG_ERROR_BAD_CREDENTIALS);
        }

        if (!userOptional.get().isConfirm()) {

            throw new DisabledException(AppConstants.MSG_ERROR_USER_NOT_CONFIRMED);
        }

        if (!userOptional.get().isEnabled()) {
            throw new DisabledException(AppConstants.MSG_ERROR_USER_DISABLED);
        }

        User user = userOptional.orElseThrow();

        List<GrantedAuthority> authorities = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(

                user.getUsername(),

                user.getPassword(),

                user.isEnabled() && user.isConfirm(),

                true,

                true,

                true,

                authorities);

    }

}
