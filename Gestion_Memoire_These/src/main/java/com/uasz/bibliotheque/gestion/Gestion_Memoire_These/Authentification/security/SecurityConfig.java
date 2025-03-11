package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.security;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] FOR_RESPONSABLE = {"/Admin/**"};
    private static final String[] FOR_STAGER = {"/User/**"};

    @Autowired
    @Lazy
    private UtilisateurService utilisateurService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/js/**", "/css/**", "/img/**").permitAll()
                        .requestMatchers("/login", "/logout", "/resultatsRecherche", "/register", "/error", "/logs", "/sessions", "/Responsable", "/notifications").permitAll()
                        .requestMatchers("/h2/**","/assets/**","/static/**","/static").permitAll()
                        .requestMatchers("/reset-password", "/reset-confirm").permitAll()
                        .requestMatchers(FOR_RESPONSABLE).hasRole("Admin")
                        .requestMatchers(FOR_STAGER).hasRole("User")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true) // Redirection après connexion
                        .successHandler((request, response, authentication) -> {
                            utilisateurService.setUserOnline(authentication.getName());
                            response.sendRedirect("/"); // Rediriger après connexion réussie
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication != null && authentication.getName() != null) {
                                utilisateurService.setUserOffline(authentication.getName());
                            }
                            response.sendRedirect("/login?logout=true");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
