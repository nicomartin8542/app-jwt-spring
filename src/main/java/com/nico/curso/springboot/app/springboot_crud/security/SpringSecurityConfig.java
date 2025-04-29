package com.nico.curso.springboot.app.springboot_crud.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.nico.curso.springboot.app.springboot_crud.security.exceptions.CustomAccessDeniedHandler;
import com.nico.curso.springboot.app.springboot_crud.security.exceptions.CustomAuthenticationEntryPoint;
import com.nico.curso.springboot.app.springboot_crud.security.filter.JwtAuthenticationFilter;
import com.nico.curso.springboot.app.springboot_crud.security.filter.JwtValidationFilter;

/**
 * Clase principal de configuración de seguridad de la aplicación.
 * 
 * @Configuration: Indica que es una clase de configuración de Spring
 * @EnableMethodSecurity: Habilita la seguridad a nivel de métodos usando
 *                        anotaciones como @PreAuthorize
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * Configura el AuthenticationManager que se usará para autenticar usuarios
     * Este bean es utilizado por los filtros JWT para validar credenciales
     */
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configura el codificador de contraseñas usando BCrypt
     * BCrypt es un algoritmo de hash seguro que incluye automáticamente un salt
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad principal
     * Define reglas de autorización, filtros JWT, CORS, CSRF y manejo de sesiones
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        authorize -> authorize.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/auth/get-user", "/api/auth/create").authenticated()
                                .anyRequest().authenticated())

                .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(confi -> confi.disable()) // Desabilitamos el token csrf, para evitar vulnerabilidades
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // para que la sesion http no tenga estado, toda la autenticacion se hace por el
                // jwt
                .build();
    }

    // Habilitar cors para permitir la comunicacion entre la aplicacion y la api
    /**
     * Configura CORS (Cross-Origin Resource Sharing)
     * Permite que el frontend en localhost:5173 pueda comunicarse con esta API
     * Define los métodos HTTP permitidos y los headers que se pueden usar
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsFilter(corsConfigurationSource()));
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

}
