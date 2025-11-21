package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.controller;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.PasswordResetToken;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.PasswordResetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class PasswordResetController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);

    @Autowired
    private PasswordResetService passwordResetService;

    /**
     * Affiche la page de demande de réinitialisation
     */
    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model) {
        logger.info("📄 Affichage de la page de demande de réinitialisation");
        return "reset-password";
    }

    /**
     * Traite la demande de réinitialisation du mot de passe
     */
    @PostMapping("/reset-password")
    public String processResetRequest(@RequestParam("email") String email,
                                      RedirectAttributes redirectAttributes) {
        logger.info("📨 Demande de réinitialisation reçue pour l'email : {}", email);

        try {
            // Créer le token et envoyer l'email
            boolean success = passwordResetService.createPasswordResetToken(email);

            if (success) {
                logger.info("✅ Token créé et email envoyé avec succès pour : {}", email);
            } else {
                logger.warn("⚠️ Aucun utilisateur trouvé pour l'email : {}", email);
            }

        } catch (Exception e) {
            logger.error("❌ Erreur lors du traitement de la demande pour {} : {}", email, e.getMessage(), e);
        }

        // Message générique pour des raisons de sécurité (ne pas révéler si l'email existe)
        redirectAttributes.addFlashAttribute("message",
                "Si l'adresse email existe dans notre système, vous recevrez un lien de réinitialisation.");
        redirectAttributes.addFlashAttribute("messageType", "info");

        return "redirect:/mess";
    }

    /**
     * Affiche la page de confirmation avec le formulaire pour le nouveau mot de passe
     */
    @GetMapping("/reset-confirm")
    public String showResetConfirmForm(@RequestParam("token") String token, Model model) {
        logger.info("🔍 Tentative de validation du token : {}", token);

        try {
            // Valider le token
            Optional<PasswordResetToken> resetToken = passwordResetService.validateToken(token);

            if (resetToken.isEmpty()) {
                logger.warn("❌ Token invalide ou expiré : {}", token);
                model.addAttribute("error", "Le lien de réinitialisation est invalide ou a expiré.");
                model.addAttribute("errorType", "token_invalid");
                return "error";
            }

            // Token valide, afficher le formulaire
            model.addAttribute("token", token);
            model.addAttribute("email", resetToken.get().getUser().getUsername());
            logger.info("✅ Token valide, affichage du formulaire de réinitialisation");

            return "reset-confirm";

        } catch (Exception e) {
            logger.error("❌ Erreur lors de la validation du token : {}", e.getMessage(), e);
            model.addAttribute("error", "Une erreur s'est produite. Veuillez réessayer.");
            return "error";
        }
    }

    /**
     * Traite la soumission du nouveau mot de passe
     */
    @PostMapping("/reset-confirm")
    public String processPasswordReset(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        logger.info("🔄 Traitement de la réinitialisation du mot de passe");

        try {
            // Vérifier que les mots de passe correspondent
            if (!password.equals(confirmPassword)) {
                logger.warn("⚠️ Les mots de passe ne correspondent pas");
                model.addAttribute("token", token);
                model.addAttribute("error", "Les mots de passe ne correspondent pas.");
                return "reset-confirm";
            }

            // Vérifier la longueur minimale du mot de passe
            if (password.length() < 6) {
                logger.warn("⚠️ Mot de passe trop court");
                model.addAttribute("token", token);
                model.addAttribute("error", "Le mot de passe doit contenir au moins 6 caractères.");
                return "reset-confirm";
            }

            // Réinitialiser le mot de passe
            boolean success = passwordResetService.resetPassword(token, password);

            if (success) {
                logger.info("✅ Mot de passe réinitialisé avec succès");
                redirectAttributes.addFlashAttribute("message",
                        "Votre mot de passe a été réinitialisé avec succès. Vous pouvez maintenant vous connecter.");
                redirectAttributes.addFlashAttribute("messageType", "success");
                return "redirect:/login";
            } else {
                logger.error("❌ Échec de la réinitialisation du mot de passe");
                model.addAttribute("error", "Le lien de réinitialisation est invalide ou a expiré.");
                return "error";
            }

        } catch (Exception e) {
            logger.error("❌ Erreur lors de la réinitialisation : {}", e.getMessage(), e);
            model.addAttribute("error", "Une erreur s'est produite lors de la réinitialisation. Veuillez réessayer.");
            return "error";
        }
    }

    /**
     * Page d'affichage des messages (info, succès, erreur)
     */
    @GetMapping("/mess")
    public String showMessages() {
        return "mess";
    }
}