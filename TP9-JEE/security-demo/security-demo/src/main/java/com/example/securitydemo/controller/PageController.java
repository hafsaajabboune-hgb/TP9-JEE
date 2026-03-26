package com.example.securitydemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Page Controller - Handles all view navigation
 * Controleur des pages - Gere la navigation entre les vues
 *
 * @author HAFSA AJABBOUNE
 * @version 2.0
 * @since March 2026
 */
@Controller
public class PageController {

    /**
     * Display custom login page
     * Affiche la page de connexion personnalisee
     *
     * @param error Indicates authentication error
     * @param logout Indicates successful logout
     * @param model View model
     * @return Login page view
     */
    @GetMapping("/authentification")
    public String showLoginPage(
            @RequestParam(value = "error", required = false) boolean error,
            @RequestParam(value = "logout", required = false) boolean logout,
            Model model) {

        if (error) {
            model.addAttribute("errorMessage", "Invalid username or password / Identifiant ou mot de passe incorrect");
        }

        if (logout) {
            model.addAttribute("successMessage", "You have been logged out successfully / Deconnexion reussie");
        }

        return "authentification";
    }

    /**
     * Display main dashboard after authentication
     * Affiche le tableau de bord principal apres authentification
     *
     * @param model View model
     * @return Dashboard view
     */
    @GetMapping("/accueil")
    public String showDashboard(Model model) {
        this.addUserInfoToModel(model);
        return "accueil";
    }

    /**
     * Admin area - Restricted to ADMINISTRATEUR role
     * Espace administration - Reserve au role ADMINISTRATEUR
     *
     * @param model View model
     * @return Admin view
     */
    @GetMapping("/administration/tableau-bord")
    public String showAdminArea(Model model) {
        this.addUserInfoToModel(model);
        return "administration";
    }

    /**
     * Management area - Accessible to ADMINISTRATEUR and GESTIONNAIRE
     * Espace gestion - Accessible aux roles ADMINISTRATEUR et GESTIONNAIRE
     *
     * @param model View model
     * @return Management view
     */
    @GetMapping("/gestion/tableau-bord")
    public String showManagementArea(Model model) {
        this.addUserInfoToModel(model);
        return "gestion";
    }

    /**
     * Consultation area - Accessible to ADMINISTRATEUR, GESTIONNAIRE and CONSULTANT
     * Espace consultation - Accessible aux roles ADMINISTRATEUR, GESTIONNAIRE et CONSULTANT
     *
     * @param model View model
     * @return Consultation view
     */
    @GetMapping("/consultation/documents")
    public String showConsultationArea(Model model) {
        this.addUserInfoToModel(model);
        return "consultation";
    }

    /**
     * Guest area - Accessible to all authenticated users
     * Espace invite - Accessible a tous les utilisateurs authentifies
     *
     * @param model View model
     * @return Guest area view
     */
    @GetMapping("/espace-invite/portail")
    public String showGuestArea(Model model) {
        this.addUserInfoToModel(model);
        return "espace-invite";
    }

    /**
     * Access denied page
     * Page d'acces refuse
     *
     * @return Access denied view
     */
    @GetMapping("/acces-refuse")
    public String showAccessDenied() {
        return "acces-refuse";
    }

    /**
     * Helper method - Add current user info to view model
     * Methode utilitaire - Ajoute les informations de l'utilisateur courant au modele
     *
     * @param model View model to populate
     */
    private void addUserInfoToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            model.addAttribute("currentUser", auth.getName());
            model.addAttribute("userRoles", auth.getAuthorities());
        }
    }
}