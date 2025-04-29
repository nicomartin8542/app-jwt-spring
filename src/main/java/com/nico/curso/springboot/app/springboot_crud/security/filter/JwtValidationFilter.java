package com.nico.curso.springboot.app.springboot_crud.security.filter;

import static com.nico.curso.springboot.app.springboot_crud.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.nico.curso.springboot.app.springboot_crud.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.nico.curso.springboot.app.springboot_crud.security.TokenJwtConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nico.curso.springboot.app.springboot_crud.security.SimpleGrantedAuthoritiesJsonCreator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro encargado de validar el token JWT en cada petición
 * Se ejecuta en todas las peticiones protegidas para verificar que el token es
 * válido
 * Extiende de BasicAuthenticationFilter para integrarse en la cadena de filtros
 * de Spring Security
 */
public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Método principal que valida el token JWT
     * 1. Extrae el token del header Authorization
     * 2. Valida la firma del token usando la clave secreta
     * 3. Extrae el username y roles del token
     * 4. Establece la autenticación en el SecurityContext si el token es válido
     * 5. Maneja errores si el token no es válido o está expirado
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Verificar si existe el header Authorization y si comienza con "Bearer "
        String header = request.getHeader(HEADER_AUTHORIZATION);
        String token = null;

        if (header != null && header.startsWith(PREFIX_TOKEN)) {
            token = header.replace(PREFIX_TOKEN, "");
        } else {
            // 2. Si no está, busca el token en las cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // Si no hay token, continuar con la cadena de filtros
        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // Intentar validar y procesar el token
            // Validar la firma del token y obtener su contenido
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            // Extraer el username y los roles del token
            String username = claims.getSubject();
            Object authoritiesClaims = claims.get("Authorities");
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper()
                    .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthoritiesJsonCreator.class)
                    .readValue(
                            authoritiesClaims.toString().getBytes(),
                            SimpleGrantedAuthority[].class

                    ));

            // Crear un objeto de autenticación con el username y roles
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null, // No necesitamos la contraseña en este punto
                    authorities);
            // Establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (JwtException e) {
            // Manejar errores de token (expirado, mal formado, firma inválida, etc)
            Map<String, String> body = new HashMap<>();
            body.put("error", "Credenciales inválidas");
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

}
