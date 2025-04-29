package com.nico.curso.springboot.app.springboot_crud.services.token;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nico.curso.springboot.app.springboot_crud.constants.AppConstants;
import com.nico.curso.springboot.app.springboot_crud.model.entities.Token;
import com.nico.curso.springboot.app.springboot_crud.model.entities.User;
import com.nico.curso.springboot.app.springboot_crud.repositories.TokenRepository;

import jakarta.transaction.Transactional;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    @Scheduled(fixedRate = AppConstants.SCHEDULE_TOKEN_CLEANUP)
    @Transactional
    public void cleanExpiredTokens() {
        tokenRepository.deleteAllExpiredTokens(new Date());
    }

    @Override
    @Transactional
    public String saveToken(User user) {
        Token tokenDb = new Token();

        try {
            Date expiryDate = Date.from(Instant.now().plus(AppConstants.TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES));
            String token = generateToken();

            tokenDb.setUser(user);
            tokenDb.setToken(token);
            tokenDb.setExpiryDate(expiryDate);
            tokenRepository.save(tokenDb);
            return token;
        } catch (Exception err) {
            System.out.println(err.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Token> verifyCode(String token) {
        Optional<Token> tokenDb = tokenRepository.findByToken(token);

        if (tokenDb.isEmpty()) {
            throw new IllegalArgumentException("Token no valido");
        }
        return tokenDb;
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        int randomNumber = secureRandom.nextInt(900000) + 100000;
        return String.format("%06d", randomNumber);
    }
}
