package com.example.securitydemo.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User Details Service Implementation
 * Implémentation du service de gestion des utilisateurs
 *
 * @author HAFSA AJABBOUNE
 * @version 2.0
 * @since March 2026
 */
@Service
public class UtilisateurService implements UserDetailsService {

    /**
     * In-memory user database
     * Base de données utilisateur en mémoire
     */
    private final Map<String, String[]> userRepository;

    /**
     * Constructor - Initializes demo users
     * Constructeur - Initialise les utilisateurs de démonstration
     */
    public UtilisateurService() {
        this.userRepository = new HashMap<>();
        this.initializeDemoUsers();
    }

    /**
     * Initialize demo accounts with unique credentials
     * Initialise les comptes de démonstration avec des identifiants uniques
     */
    private void initializeDemoUsers() {

        // Account 1: System Administrator - Full access
        // Compte 1: Administrateur système - Accès complet
        this.userRepository.put("hafsa.ajabboune",
                new String[]{"{noop}Xk9#mP2$wL5@", "ROLE_ADMINISTRATEUR", "ROLE_GESTIONNAIRE", "ROLE_CONSULTANT", "ROLE_INVITE"});

        // Account 2: Senior Manager - Management and consultation
        // Compte 2: Gestionnaire senior - Gestion et consultation
        this.userRepository.put("fatimaazahra.aithassou",
                new String[]{"{noop}Yt7&bN4#rM1@", "ROLE_GESTIONNAIRE", "ROLE_CONSULTANT", "ROLE_INVITE"});

        // Account 3: Lead Consultant - Read-only access
        // Compte 3: Consultante principale - Accès en lecture
        this.userRepository.put("sara.elaatifi",
                new String[]{"{noop}Zw3*cV8$pL6!", "ROLE_CONSULTANT", "ROLE_INVITE"});

        // Account 4: Guest User - Limited access
        // Compte 4: Utilisatrice invitée - Accès limité
        this.userRepository.put("loubna.elghazi",
                new String[]{"{noop}Ue5@jR2#kW7$", "ROLE_INVITE"});

        this.displayStartupInformation();
    }

    /**
     * Display all available accounts in console at startup
     * Affiche tous les comptes disponibles dans la console au démarrage
     */
    private void displayStartupInformation() {
        System.out.println("\n==================================================");
        System.out.println("USER AUTHENTICATION SERVICE - INITIALIZED");
        System.out.println("SERVICE D'AUTHENTIFICATION - INITIALISE");
        System.out.println("==================================================");

        for (Map.Entry<String, String[]> entry : this.userRepository.entrySet()) {
            String username = entry.getKey();
            String password = entry.getValue()[0].replace("{noop}", "");

            System.out.println("\n--------------------------------------------------");
            System.out.println("Username / Identifiant : " + username);
            System.out.println("Password / Mot de passe : " + password);
            System.out.print("Roles / Roles : ");

            for (int i = 1; i < entry.getValue().length; i++) {
                String role = entry.getValue()[i].replace("ROLE_", "");
                System.out.print(role);
                if (i < entry.getValue().length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("\n--------------------------------------------------");
        }

        System.out.println("\n==================================================");
        System.out.println("Application ready - Connect to test your access rights");
        System.out.println("Application prete - Connectez-vous pour tester vos droits");
        System.out.println("==================================================\n");
    }

    /**
     * Load user by username for Spring Security authentication
     * Charge un utilisateur par son identifiant pour l'authentification Spring Security
     *
     * @param username The username provided in login form
     * @return UserDetails object containing authentication information
     * @throws UsernameNotFoundException If user does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("\nAuthentication attempt / Tentative d'authentification : " + username);

        String[] userInfo = this.userRepository.get(username);

        if (userInfo == null) {
            System.err.println("Authentication failed - Unknown user / Echec - Utilisateur inconnu : " + username);
            throw new UsernameNotFoundException("User not found / Utilisateur non trouve : " + username);
        }

        String password = userInfo[0];
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        System.out.println("Loading roles / Chargement des roles for / pour " + username + " :");
        for (int i = 1; i < userInfo.length; i++) {
            authorities.add(new SimpleGrantedAuthority(userInfo[i]));
            System.out.println("  -> " + userInfo[i]);
        }

        System.out.println("Authentication successful / Authentification reussie : " + username + "\n");

        return User.builder()
                .username(username)
                .password(password)
                .authorities(authorities)
                .build();
    }

    /**
     * Check if user exists in database
     * Verifie si un utilisateur existe dans la base de donnees
     *
     * @param username The username to check
     * @return true if user exists
     */
    public boolean userExists(String username) {
        return this.userRepository.containsKey(username);
    }

    /**
     * Get all available usernames
     * Recupere tous les identifiants disponibles
     *
     * @return Array of usernames
     */
    public String[] getAllUsernames() {
        return this.userRepository.keySet().toArray(new String[0]);
    }
}