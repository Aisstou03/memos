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
    private static final String[] FOR_RESPONSABLE = {"/Responsable/**"};
    private static final String[] FOR_STAGER = {"/Stager/**"};
    private static final String[] FOR_SUPERADMIN = {"/Admin/**"};

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
                        // Pages communes accessibles à tout le monde (que ce soit Responsable ou Stager)
                        .requestMatchers("/js/**", "/css/**", "/img/**", "/login", "/logout","/reset-password").permitAll()
                        .requestMatchers("/reset-password/**","/profil/**", "/dashboard/**").permitAll()  // Page profil et tableau de bord sont communes
                        .requestMatchers("/js/**", "/css/**", "/img/**", "/static/**", "/assets/**").permitAll()

                        // Pages réservées aux Responsables (Admin)
                        .requestMatchers("/memoires/liste").hasAnyRole("Admin", "Responsable")

                        // Pages réservées aux Stagers
                        .requestMatchers("/dashboard/stager").hasRole("stager")  // Seuls les Stagers peuvent accéder à leur tableau de bord


                        // Toute autre requête doit être authentifiée
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/access-denied")  // Page d'erreur personnalisée
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true) // Redirection après connexion réussie
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.sendRedirect("/login?logout=true");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}