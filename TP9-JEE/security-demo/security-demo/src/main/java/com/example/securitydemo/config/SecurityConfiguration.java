package com.example.securitydemo.config;

import com.example.securitydemo.service.UtilisateurService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration Class
 * Classe de configuration de la securite
 *
 * @author HAFSA AJABBOUNE
 * @version 2.0
 * @since March 2026
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final UtilisateurService userService;

    /**
     * Constructor with dependency injection
     * Constructeur avec injection de dependance
     *
     * @param userService User management service
     */
    public SecurityConfiguration(UtilisateurService userService) {
        this.userService = userService;
    }

    /**
     * Configure security filter chain
     * Configure la chaine de filtres de securite
     *
     * @param http HttpSecurity object for configuration
     * @return SecurityFilterChain configured filter chain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Authorization rules / Regles d'autorisation
                .authorizeHttpRequests(requests -> requests
                        // Public resources - accessible without authentication
                        // Ressources publiques - accessibles sans authentification
                        .requestMatchers("/authentification", "/css/**", "/js/**", "/images/**").permitAll()

                        // Role-based access control / Controle d'acces base sur les roles
                        .requestMatchers("/administration/**").hasRole("ADMINISTRATEUR")
                        .requestMatchers("/gestion/**").hasAnyRole("ADMINISTRATEUR", "GESTIONNAIRE")
                        .requestMatchers("/consultation/**").hasAnyRole("ADMINISTRATEUR", "GESTIONNAIRE", "CONSULTANT")
                        .requestMatchers("/espace-invite/**").hasAnyRole("INVITE", "CONSULTANT", "GESTIONNAIRE", "ADMINISTRATEUR")
                        .anyRequest().authenticated()
                )

                // Custom login form configuration
                // Configuration du formulaire de connexion personnalise
                .formLogin(login -> login
                        .loginPage("/authentification")
                        .loginProcessingUrl("/authenticate")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/accueil", true)
                        .permitAll()
                )

                // Logout configuration
                // Configuration de la deconnexion
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/authentification?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("USER_SESSION")
                        .permitAll()
                )

                // Access denied handling
                // Gestion de l'acces refuse
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedPage("/acces-refuse")
                );

        return http.build();
    }
}