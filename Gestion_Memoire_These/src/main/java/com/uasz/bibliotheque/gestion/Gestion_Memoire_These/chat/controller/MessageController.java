package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.controller;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.model.Message;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Afficher les messages dans une page Thymeleaf
    @GetMapping("/messages")
    public String getMessages(Model model, Principal principal) {
        model.addAttribute("messages", messageService.getMessages());
        model.addAttribute("currentUser", principal.getName()); // Ajouter l'utilisateur actuel
        return "messages";
    }

    // Renvoyer les messages au format JSON
    @GetMapping("/api/messages")
    @ResponseBody
    public List<Message> getMessagesJson() {
        return messageService.getMessages();
    }

    // Obtenir le nombre de messages non lus
    @GetMapping("/api/messages/unread")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> getUnreadCount(@RequestParam Long lastSeenId) {
        List<Message> messages = messageService.getMessages();
        long count = messages.stream()
                .filter(message -> message.getId() > lastSeenId)
                .count();

        return ResponseEntity.ok(Map.of("count", (int) count));
    }

    @PostMapping("/messages")
    public String envoyerMessage(@RequestParam String contenu, Principal principal) {
        Utilisateur auteur = utilisateurRepository.findByUsername(principal.getName());
        if (auteur == null) {
            throw new RuntimeException("Utilisateur non trouvé !");
        }
        Message message = new Message();
        message.setContenu(contenu);
        message.setAuteur(auteur);
        messageService.envoyerMessage(message);
        return "redirect:/messages";
    }

    // API pour envoyer un message (pour les requêtes AJAX)
    @PostMapping("/api/messages")
    @ResponseBody
    public ResponseEntity<Message> envoyerMessageApi(@RequestParam String contenu, Principal principal) {
        Utilisateur auteur = utilisateurRepository.findByUsername(principal.getName());
        if (auteur == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Message message = new Message();
        message.setContenu(contenu);
        message.setAuteur(auteur);
        Message savedMessage = messageService.envoyerMessage(message);

        return ResponseEntity.ok(savedMessage);
    }

    // Supprimer un message
    @DeleteMapping("/messages/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteMessage(@PathVariable Long id, Principal principal) {
        Message message = messageService.getMessageById(id);
        if (message != null && message.getAuteur().getUsername().equals(principal.getName())) {
            messageService.supprimerMessage(id);
            return ResponseEntity.ok("Message supprimé");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Action non autorisée");
    }

    // Modifier un message
    @PutMapping("/messages/{id}")
    @ResponseBody
    public ResponseEntity<String> editMessage(@PathVariable Long id, @RequestBody(required = false) Map<String, String> payload,
                                              @RequestParam(required = false) String contenu, Principal principal) {
        // Permet de gérer les deux formats de requête (JSON et form-data)
        String newContent = payload != null ? payload.get("contenu") : contenu;

        if (newContent == null || newContent.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Le contenu ne peut pas être vide");
        }

        Message message = messageService.getMessageById(id);
        if (message != null && message.getAuteur().getUsername().equals(principal.getName())) {
            message.setContenu(newContent);
            messageService.modifierMessage(message);
            return ResponseEntity.ok("Message modifié");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Action non autorisée");
    }
}