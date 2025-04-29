package com.nico.curso.springboot.app.springboot_crud.security.filter;

import static com.nico.curso.springboot.app.springboot_crud.security.TokenJwtConfig.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nico.curso.springboot.app.springboot_crud.constants.AppConstants;
import com.nico.curso.springboot.app.springboot_crud.model.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro encargado del proceso de autenticación (login)
 * Se activa cuando se hace una petición POST al endpoint de login
 * Extiende de UsernamePasswordAuthenticationFilter para manejar autenticación
 * básica
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Método que procesa el intento de autenticación
     * 1. Lee el cuerpo de la petición y lo convierte en un objeto User
     * 2. Extrae username y password
     * 3. Delega la autenticación al AuthenticationManager
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        User user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    }

    /**
     * Método ejecutado cuando la autenticación es exitosa
     * 1. Extrae el username y roles del usuario autenticado
     * 2. Genera un token JWT con esta información
     * 3. Agrega el token en el header de la respuesta
     * 4. Envía una respuesta JSON con el token y datos del usuario
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        // Genero el token si todo esta ok
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal())
                .getUsername();

        // Obtengo los roles y los agrego al token por la opcion claims
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
        Claims claims = Jwts.claims().add("Authorities", new ObjectMapper().writeValueAsString(roles)).build();

        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 36000000)) // Token expirado en 1 hora
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("token", token);
        body.put("message", "Token generado con exito");

        // ... Establezco el token Cookies (HttpOnly)
        Cookie cookie = new Cookie(AppConstants.COOKIE_NAME, token);
        cookie.setHttpOnly(true); // Evita acceso por JS
        cookie.setSecure(false); // Solo por HTTPS en producción
        cookie.setPath("/"); // Disponible para toda la app
        cookie.setMaxAge(60 * 60); // 1 hora (ajusta según tu necesidad)

        response.addCookie(cookie);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);

    }

    /**
     * Método ejecutado cuando la autenticación falla
     * Envía una respuesta de error con código 401 (Unauthorized)
     * Incluye un mensaje de error y los detalles del fallo
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        Map<String, String> body = new HashMap<>();

        if (failed.getMessage().equals("Bad credentials")) {
            body.put("error", AppConstants.MSG_ERROR_BAD_CREDENTIALS);
        } else {
            body.put("error", failed.getMessage());
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);

    }
}