package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.PasswordResetToken;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.PasswordResetTokenRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int TOKEN_EXPIRY_HOURS = 24;

    @Autowired
    private UtilisateurRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.base.url:http://localhost:8080}")
    private String baseUrl;

    /**
     * Crée un token de réinitialisation et envoie l'email
     */
    @Transactional
    public boolean createPasswordResetToken(String email) {
        try {
            logger.info("🔍 Recherche de l'utilisateur avec l'email : {}", email);

            // Rechercher l'utilisateur
            Utilisateur user = userRepository.findByUsername(email);

            if (user == null) {
                logger.warn("⚠️ Aucun utilisateur trouvé avec l'email : {}", email);
                return false;
            }

            logger.info("✅ Utilisateur trouvé : {} (ID: {})", user.getNom(), user.getId());

            // Invalider les anciens tokens non utilisés
            invalidateOldTokens(user);

            // Générer un nouveau token unique
            String token = UUID.randomUUID().toString();
            logger.info("🔑 Token généré : {}", token);

            // Créer l'entité PasswordResetToken
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS));
            resetToken.setUsed(false);

            // Sauvegarder le token
            tokenRepository.save(resetToken);
            logger.info("💾 Token sauvegardé en base de données");

            // Construire l'URL de réinitialisation
            String resetUrl = baseUrl + "/reset-confirm?token=" + token;
            logger.info("🔗 URL de réinitialisation : {}", resetUrl);

            // Envoyer l'email
            try {
                emailService.sendPasswordResetEmail(user.getUsername(), user.getNom(), resetUrl);
                logger.info("✅ Email de réinitialisation envoyé avec succès à : {}", user.getUsername());
                return true;
            } catch (Exception e) {
                logger.error("❌ Erreur lors de l'envoi de l'email à {} : {}", user.getUsername(), e.getMessage(), e);
                // Supprimer le token si l'email n'a pas pu être envoyé
                tokenRepository.delete(resetToken);
                throw new RuntimeException("Échec de l'envoi de l'email de réinitialisation", e);
            }

        } catch (Exception e) {
            logger.error("❌ Erreur lors de la création du token de réinitialisation : {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Vérifie si un token est valide
     */
    public Optional<PasswordResetToken> validateToken(String token) {
        logger.info("🔍 Validation du token : {}", token);

        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);

        if (resetToken.isEmpty()) {
            logger.warn("⚠️ Token non trouvé : {}", token);
            return Optional.empty();
        }

        PasswordResetToken tokenEntity = resetToken.get();

        if (tokenEntity.isExpired()) {
            logger.warn("⏰ Token expiré : {} (Date d'expiration : {})", token, tokenEntity.getExpiryDate());
            return Optional.empty();
        }

        if (tokenEntity.isUsed()) {
            logger.warn("🔒 Token déjà utilisé : {}", token);
            return Optional.empty();
        }

        logger.info("✅ Token valide : {}", token);
        return resetToken;
    }

    /**
     * Réinitialise le mot de passe avec le token
     */
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        try {
            logger.info("🔄 Tentative de réinitialisation du mot de passe avec le token : {}", token);

            Optional<PasswordResetToken> resetTokenOpt = validateToken(token);

            if (resetTokenOpt.isEmpty()) {
                logger.error("❌ Token invalide ou expiré : {}", token);
                return false;
            }

            PasswordResetToken resetToken = resetTokenOpt.get();
            Utilisateur user = resetToken.getUser();

            // Encoder et sauvegarder le nouveau mot de passe
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);

            logger.info("✅ Mot de passe mis à jour pour l'utilisateur : {}", user.getUsername());

            // Marquer le token comme utilisé
            resetToken.setUsed(true);
            tokenRepository.save(resetToken);

            logger.info("🔒 Token marqué comme utilisé : {}", token);

            return true;

        } catch (Exception e) {
            logger.error("❌ Erreur lors de la réinitialisation du mot de passe : {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Invalide les anciens tokens non utilisés d'un utilisateur
     */
    @Transactional
    private void invalidateOldTokens(Utilisateur user) {
        List<PasswordResetToken> oldTokens = tokenRepository.findValidTokensByUser(user, LocalDateTime.now());

        if (!oldTokens.isEmpty()) {
            logger.info("🗑️ Invalidation de {} ancien(s) token(s) pour l'utilisateur : {}", oldTokens.size(), user.getUsername());
            oldTokens.forEach(token -> token.setUsed(true));
            tokenRepository.saveAll(oldTokens);
        }
    }

    /**
     * Nettoie les tokens expirés (à exécuter périodiquement)
     */
    @Transactional
    public void cleanExpiredTokens() {
        logger.info("🧹 Nettoyage des tokens expirés");
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}