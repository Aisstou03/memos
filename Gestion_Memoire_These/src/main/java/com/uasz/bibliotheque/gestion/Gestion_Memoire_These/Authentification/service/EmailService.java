package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Envoie un email de création de compte
     */
    public void sendAccountCreationEmail(String toEmail, String email, String password) {
        if (!isValidEmail(toEmail)) {
            logger.error("Adresse e-mail invalide pour la création de compte : {}", toEmail);
            throw new IllegalArgumentException("L'adresse e-mail du destinataire est invalide.");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Création de votre compte - Bibliothèque UASZ");
            message.setText("Bonjour,\n\nVotre compte a été créé avec succès.\n\n" +
                    "Email : " + email + "\n" +
                    "Mot de passe : " + password + "\n\n" +
                    "Cordialement,\nL'équipe de la bibliothèque numérique de l'UASZ");

            logger.info("📧 Envoi de l'email de création de compte à : {}", toEmail);
            mailSender.send(message);
            logger.info("✅ Email de création envoyé avec succès à : {}", toEmail);
        } catch (MailException e) {
            logger.error("❌ Erreur lors de l'envoi de l'email de création à {} : {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Échec de l'envoi de l'email de création : " + e.getMessage(), e);
        }
    }

    /**
     * Envoie un email de suppression de compte
     */
    public void sendAccountDeletionEmail(String toEmail) {
        if (!isValidEmail(toEmail)) {
            logger.error("Adresse e-mail invalide pour la suppression : {}", toEmail);
            throw new IllegalArgumentException("L'adresse e-mail du destinataire est invalide.");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Suppression de votre compte - Bibliothèque UASZ");
            message.setText("Bonjour,\n\n" +
                    "Nous vous informons que votre compte a été supprimé et que vous n'avez plus accès à notre site.\n\n" +
                    "Si vous pensez qu'il s'agit d'une erreur ou si vous avez des questions, n'hésitez pas à nous contacter.\n\n" +
                    "Cordialement,\nL'équipe de la bibliothèque numérique de l'UASZ");

            logger.info("📧 Envoi de l'email de suppression à : {}", toEmail);
            mailSender.send(message);
            logger.info("✅ Email de suppression envoyé avec succès à : {}", toEmail);
        } catch (MailException e) {
            logger.error("❌ Erreur lors de l'envoi de l'email de suppression à {} : {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Échec de l'envoi de l'email de suppression : " + e.getMessage(), e);
        }
    }

    /**
     * Envoie un email de réinitialisation de mot de passe
     */
    public void sendPasswordResetEmail(String toEmail, String userName, String resetUrl) {
        if (!isValidEmail(toEmail)) {
            logger.error("Adresse e-mail invalide pour la réinitialisation : {}", toEmail);
            throw new IllegalArgumentException("L'adresse e-mail du destinataire est invalide.");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Réinitialisation de mot de passe - Bibliothèque UASZ");

            String emailBody = "Bonjour " + userName + ",\n\n" +
                    "Vous avez demandé la réinitialisation de votre mot de passe pour accéder à la bibliothèque numérique de l'UASZ.\n\n" +
                    "Veuillez cliquer sur le lien ci-dessous pour définir un nouveau mot de passe :\n\n" +
                    resetUrl + "\n\n" +
                    "Ce lien est valable pendant 24 heures. Après ce délai, vous devrez faire une nouvelle demande.\n\n" +
                    "Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet email et votre mot de passe restera inchangé.\n\n" +
                    "Cordialement,\n" +
                    "L'équipe de la bibliothèque numérique de l'UASZ";

            message.setText(emailBody);

            logger.info("📧 Envoi de l'email de réinitialisation de mot de passe à : {}", toEmail);
            mailSender.send(message);
            logger.info("✅ Email de réinitialisation envoyé avec succès à : {}", toEmail);
        } catch (MailException e) {
            logger.error("❌ Erreur lors de l'envoi de l'email de réinitialisation à {} : {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Échec de l'envoi de l'email de réinitialisation : " + e.getMessage(), e);
        }
    }

    /**
     * Méthode générique pour envoyer un email (conservée pour compatibilité)
     */
    public void sendEmaile(String toEmail, String subject, String body) {
        if (!isValidEmail(toEmail)) {
            logger.error("Adresse e-mail invalide : {}", toEmail);
            throw new IllegalArgumentException("L'adresse e-mail du destinataire est invalide.");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            logger.info("📧 Envoi de l'email à : {}", toEmail);
            mailSender.send(message);
            logger.info("✅ Email envoyé avec succès à : {}", toEmail);
        } catch (MailException e) {
            logger.error("❌ Erreur lors de l'envoi de l'email à {} : {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Échec de l'envoi de l'email : " + e.getMessage(), e);
        }
    }

    /**
     * Valide le format d'une adresse email
     */
    private boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() && email.contains("@");
    }
}