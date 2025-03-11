package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendAccountCreationEmail(String toEmail, String email, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Création de votre compte");
            message.setText("Bonjour,\n\nVotre compte a été créé avec succès.\n\n" +
                    "Email : " + email + "\n" +
                    "Mot de passe : " + password + "\n\n" +
                    "Cordialement,\nL'équipe.");

            logger.info("Envoi de l'email à {}", toEmail);
            mailSender.send(message);
            logger.info("Email envoyé avec succès !");
        } catch (MailException e) {
            logger.error("Erreur lors de l'envoi de l'email : {}", e.getMessage());
        }
    }

    public void sendAccountDeletionEmail(String toEmail) {
        if (toEmail == null || toEmail.isEmpty()) {
            throw new IllegalArgumentException("L'adresse e-mail du destinataire est invalide.");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Suppression de votre compte");

            message.setText("Bonjour,\n\n" +
                    "Nous vous informons que votre compte a été supprimé et que vous n’avez plus accès à notre site.\n\n" +
                    "Si vous pensez qu'il s'agit d'une erreur ou si vous avez des questions, n’hésitez pas à nous contacter.\n\n" +
                    "Cordialement,\nL'équipe.");

            mailSender.send(message);
            logger.info("Email de suppression envoyé avec succès à {}", toEmail);
        } catch (MailException e) {
            logger.error("Erreur lors de l'envoi de l'e-mail de suppression : {}", e.getMessage());
        }
    }

    public void sendEmaile(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            logger.info("Envoi de l'email à {}", toEmail);
            mailSender.send(message);
            logger.info("Email envoyé avec succès !");
        } catch (MailException e) {
            logger.error("Erreur lors de l'envoi de l'email : {}", e.getMessage());
        }
    }


}