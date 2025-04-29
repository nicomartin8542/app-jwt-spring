package com.nico.curso.springboot.app.springboot_crud.services.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nico.curso.springboot.app.springboot_crud.model.dto.token.TokenRequestDto;
import com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth.UserData;
import com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth.UserNewPassword;
import com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth.UserReSendEmail;
import com.nico.curso.springboot.app.springboot_crud.model.dto.userAuth.UserRigisterDto;
import com.nico.curso.springboot.app.springboot_crud.model.entities.Role;
import com.nico.curso.springboot.app.springboot_crud.model.entities.Token;
import com.nico.curso.springboot.app.springboot_crud.model.entities.User;
import com.nico.curso.springboot.app.springboot_crud.repositories.RolRepository;
import com.nico.curso.springboot.app.springboot_crud.repositories.TokenRepository;
import com.nico.curso.springboot.app.springboot_crud.repositories.UserRepository;
import com.nico.curso.springboot.app.springboot_crud.constants.AppConstants;
import com.nico.curso.springboot.app.springboot_crud.services.token.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.nico.curso.springboot.app.springboot_crud.services.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${front.url}")
    private String URL_FRONT;

    @Scheduled(cron = "0 0 3 * * ?") // Todos los días a las 3:00 AM
    @Transactional
    public void limpiarUsuariosNoConfirmados() {
        List<User> usuariosNoConfirmados = userRepository.findAllByConfirmFalse();
        for (User user : usuariosNoConfirmados) {
            userRepository.delete(user);
        }
    }

    @Override
    public UserData getUserData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        UserData userData = new UserData();
        userData.setUsername(user.get().getUsername());
        userData.setName(user.get().getName());
        userData.setRoles(user.get().getRoles());
        return userData;
    }

    @Override
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(AppConstants.COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Elimina la cookie
        response.addCookie(cookie);
    }

    @Override
    @Transactional
    public String save(User user) {
        User newUser = new User();

        if (user.getPasswordConfirmation() == null) {
            throw new IllegalArgumentException("Confirm password is required");
        }

        if (!user.getPasswordConfirmation().equals(user.getPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Validar longitud del password antes del encode
        String password = user.getPassword();
        if (password.length() < AppConstants.PASSWORD_MIN_LENGTH
                || password.length() > AppConstants.PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException("La password debe tener entre 8 y 20 caracteres");
        }

        UserRigisterDto userDto = new UserRigisterDto();

        Optional<Role> rol = rolRepository.findByName(AppConstants.ROLE_USER);
        List<Role> roles = new ArrayList<>();
        rol.ifPresent(roles::add);

        // Si es admin, agrego el rol admin
        if (user.isAdmin()) {
            Optional<Role> rolAdmin = rolRepository.findByName(AppConstants.ROLE_ADMIN);
            rolAdmin.ifPresent(roles::add);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        newUser = userRepository.save(user);

        String token = tokenService.saveToken(newUser);

        try {
            String cuerpo = "Has solicitado un nuevo código de verificación para confirmar tu cuenta.";
            String url = "/auth/confirm-account";
            String subject = "Código de verificación";
            String name = "Bienvenido, " + newUser.getName() + "!";
            emailService.sendHtmlEmail(newUser.getUsername(), name, token, subject, cuerpo, url);
        } catch (MessagingException e) {
            // Loguear o manejar error de envío de correo
            throw new RuntimeException(
                    "No se pudo enviar el correo de verificacion. Verifica si el correo ingresado es correcto y vuelve a intentarlo.");
        }

        userDto.setUsername(newUser.getUsername());
        return "Cuenta creada correctamente";
    }

    @Override
    @Transactional
    public Optional<String> confirmAccount(String token) {
        Optional<Token> tokenDb = tokenService.verifyCode(token);

        if (tokenDb.isEmpty()) {
            throw new IllegalArgumentException("Token no valido.");
        }

        User userDb = tokenDb.get().getUser();
        userDb.setConfirm(true);
        userDb.setEnabled(true);
        userRepository.save(userDb);

        tokenRepository.delete(tokenDb.get());

        return Optional.of("Cuenta confirmada");
    }

    @Override
    @Transactional
    public String forgotPassword(UserReSendEmail user) {
        Optional<User> userDb = userRepository.findByUsername(user.getUsername());
        if (userDb.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado! Verifique y vuelva a intentar.");
        }
        if (!userDb.get().isConfirm()) {
            throw new IllegalArgumentException("La cuenta no ha sido confirmada.");
        }
        if (!userDb.get().isEnabled()) {
            throw new IllegalArgumentException("La cuenta esta deshabilitada.");
        }

        System.out.println(user.getUsername());
        String token = tokenService.saveToken(userDb.get());

        try {
            String cuerpo = "Has solicitado el cambio de contraseña.";
            String url = "/auth/new-password";
            String subject = "Cambio de contraseña";
            String name = "Bienvenido, " + userDb.get().getName() + "!";
            emailService.sendHtmlEmail(userDb.get().getUsername(), name, token,
                    subject, cuerpo, url);
        } catch (MessagingException e) {
            // Loguear o manejar error de envío de correo
            throw new RuntimeException(
                    "No se pudo enviar el correo de verificacion. Verifica si el correo ingresado es correcto y vuelve a intentarlo.");
        }
        return "Se envio un correo para el cambio de contraseña. Verifique su Email.";
    }

    @Override
    @Transactional
    public String resendEmailBycode(UserReSendEmail user) {

        Optional<User> userDb = userRepository.findByUsername(user.getUsername());

        if (userDb.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado! Verifique y vuelva a intentar.");
        }

        if (userDb.get().isConfirm()) {
            throw new IllegalArgumentException("La cuenta ya ha sido confirmada.");
        }

        if (!userDb.get().isEnabled()) {
            throw new IllegalArgumentException("La cuenta esta bloqueda!");
        }

        // Valido que no haya superado el limite de tokens activos
        long tokensActivos = userDb.get().getTokens().stream()
                .filter(token -> token.getExpiryDate().after(new Date()))
                .count();

        if (tokensActivos >= AppConstants.TOKEN_LIMIT) {
            throw new IllegalArgumentException("Has superado el límite de solicitudes de código. Intenta más tarde.");
        }

        // Valido que no haya superado el limite de tokens activos en los ultimos 2
        // minutos
        Date haceCincoMin = Date.from(Instant.now().minus(AppConstants.TOKEN_LIMIT_MINUTES, ChronoUnit.MINUTES));
        long tokensUltimosMin = userDb.get().getTokens().stream()
                .filter(token -> token.getCreatedAt().after(haceCincoMin))
                .count();

        if (tokensUltimosMin >= 1) {
            throw new IllegalArgumentException(
                    "Solo puedes solicitar un código cada " + AppConstants.TOKEN_LIMIT_MINUTES + " minutos.");
        }

        String token = tokenService.saveToken(userDb.get());
        try {
            String cuerpo = "Has solicitado un nuevo código de verificación para confirmar tu cuenta.";
            String url = "/auth/confirm-account";
            String subject = "Código de verificación";
            emailService.sendHtmlEmail(userDb.get().getUsername(), userDb.get().getName(), token,
                    subject, cuerpo, url);
        } catch (MessagingException e) {
            // Loguear o manejar error de envío de correo
            throw new RuntimeException(
                    "No se pudo enviar el correo de verificacion. Verifica si el correo ingresado es correcto y vuelve a intentarlo.");

        }
        return "Correo de verificacion reenviado. Verifique su Email.";
    }

    @Override
    public String verifyToken(TokenRequestDto token) {
        Optional<Token> tokenDb = tokenService.verifyCode(token.getToken());

        if (tokenDb.isEmpty()) {
            throw new IllegalArgumentException("Token no valido.");
        }

        return "Token valido";
    }

    @Override
    public String resetPassword(UserNewPassword user, String token) {

        Optional<Token> tokenDb = tokenService.verifyCode(token);
        String password = user.getPassword();
        String passwordConfirmation = user.getPasswordConfirmation();

        if (tokenDb.isEmpty()) {
            throw new IllegalArgumentException("Token no valido.");
        }

        if (passwordConfirmation == null) {
            throw new IllegalArgumentException("Confirm password is required");
        }

        if (!passwordConfirmation.equals(password)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Validar longitud del password antes del encode
        if (password.length() < AppConstants.PASSWORD_MIN_LENGTH
                || password.length() > AppConstants.PASSWORD_MAX_LENGTH) {
            throw new IllegalArgumentException("La password debe tener entre 8 y 20 caracteres");
        }

        Optional<User> userDb = userRepository.findById(tokenDb.get().getUser().getId());

        if (userDb.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado! Verifique y vuelva a intentar.");
        }

        userDb.get().setPassword(passwordEncoder.encode(password));
        userRepository.save(userDb.get());

        tokenRepository.delete(tokenDb.get());

        return "Se cambio la contraseña correctamente.";
    }
}
