package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.controller;



import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.PasswordResetTokenRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.EmailService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    private UtilisateurRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UtilisateurService userService;

    // Afficher la page de demande de réinitialisation
    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        return "reset-password"; // Page pour saisir l'email
    }

    // Traiter la demande de réinitialisation
    // Traiter la réinitialisation du mot de passe
    @PostMapping("/reset-password")
    public String processResetRequest(@RequestParam("email") String email, Model model, RedirectAttributes redirectAttributes) {
        // Chercher l'utilisateur par email
        Utilisateur user = userRepository.findByUsername(email);

        if (user != null) {
            // Générer un token unique
            String token = UUID.randomUUID().toString();

            // Créer un token de réinitialisation valide pendant 24 heures
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(24));

            // Sauvegarder le token dans la base de données
            tokenRepository.save(resetToken);

            // Construire le lien de réinitialisation
            String resetUrl = "http://localhost:8080/reset-confirm?token=" + token;

            // Envoyer l'email
            String emailBody = "Bonjour " + user.getNom() + ",\n\n" +
                    "Vous avez demandé la réinitialisation de votre mot de passe pour accéder à la bibliothèque numérique de l'UASZ. " +
                    "Veuillez cliquer sur le lien ci-dessous pour définir un nouveau mot de passe :\n\n" +
                    resetUrl + "\n\n" +
                    "Ce lien est valable pendant 24 heures. Après ce délai, vous devrez faire une nouvelle demande.\n\n" +
                    "Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet email.\n\n" +
                    "Cordialement,\n" +
                    "L'équipe de la bibliothèque numérique de l'UASZ";

            emailService.sendEmaile(user.getUsername(), "Réinitialisation de mot de passe - Bibliothèque UASZ", emailBody);
        }

        // Toujours afficher un message de succès, même si l'email n'existe pas (sécurité)
        redirectAttributes.addFlashAttribute("message", "Si l'adresse email existe dans notre système, vous recevrez un lien de réinitialisation.");
        return "redirect:/messages";
    }


    // Afficher la page de confirmation avec le formulaire pour le nouveau mot de passe
    @GetMapping("/reset-confirm")
    public String showResetConfirmForm(@RequestParam("token") String token, Model model) {
        // Vérifier si le token existe et est valide
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null || resetToken.isExpired()) {
            model.addAttribute("error", "Le lien de réinitialisation est invalide ou a expiré.");
            return "error";
        }

        model.addAttribute("token", token);
        return "reset-confirm"; // Page pour saisir le nouveau mot de passe
    }

  }