package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.controller;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.model.Notification;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Afficher la page des notifications
    @GetMapping
    public String afficherNotifications(Model model) {
        List<Notification> notifications = notificationService.getNotificationNonLue();
        model.addAttribute("notifications", notifications);
        return "notifications"; // Vue Thymeleaf pour afficher les notifications
    }

    // Récupérer les notifications en AJAX pour le popup
    @GetMapping("/api/liste")
    @ResponseBody
    public List<Notification> getNotificationsJson() {
        return notificationService.getNotificationNonLue();
    }

    // Récupérer le nombre de notifications non lues
    @GetMapping("/api/count")
    @ResponseBody
    public Map<String, Integer> getNotificationCount() {
        int count = notificationService.getNotificationNonLue().size();
        return Map.of("count", count);
    }

    // Marquer une notification comme lue
    @PostMapping("/lire/{id}")
    public String marquerCommeLue(@PathVariable Long id) {
        notificationService.marquerCommeLue(id);
        return "redirect:/notifications";
    }

    // Marquer comme lue en AJAX
    @PostMapping("/api/lire/{id}")
    @ResponseBody
    public ResponseEntity<?> marquerCommeLueAjax(@PathVariable Long id) {
        notificationService.marquerCommeLue(id);
        return ResponseEntity.ok().build();
    }

    // Supprimer une notification
    @PostMapping("/supprimer/{id}")
    public String supprimerNotification(@PathVariable Long id) {
        notificationService.supprimerNotification(id);
        return "redirect:/notifications";
    }

    // Supprimer en AJAX
    @PostMapping("/api/supprimer/{id}")
    @ResponseBody
    public ResponseEntity<?> supprimerNotificationAjax(@PathVariable Long id) {
        notificationService.supprimerNotification(id);
        return ResponseEntity.ok().build();
    }
}