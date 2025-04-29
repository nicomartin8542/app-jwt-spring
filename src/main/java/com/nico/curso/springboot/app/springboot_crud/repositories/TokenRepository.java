package com.nico.curso.springboot.app.springboot_crud.repositories;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nico.curso.springboot.app.springboot_crud.model.entities.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Modifying
    @Query("delete from Token t where t.expiryDate < ?1")
    void deleteAllExpiredTokens(Date now);

    @Query("select t from Token t where trim(t.token) = ?1")
    Optional<Token> findByToken(String token);

}
