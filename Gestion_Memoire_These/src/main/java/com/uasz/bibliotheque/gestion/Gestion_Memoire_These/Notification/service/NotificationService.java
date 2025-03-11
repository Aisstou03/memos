package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.model.Notification;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    //Mehode pour creer une notification en recuperant le message en paramettre
    public void creerNotification(String message) {
        System.out.println("Tentative de création de notification : " + message);
        Notification notification = new Notification(message);
        notificationRepository.save(notification);
        System.out.println("Notification sauvegardée avec succès !");
    }


    //Methode pour recuperer les message non lue
    public List<Notification> getNotificationNonLue(){
        return notificationRepository.findByLueFalse();
    }

    //Methode pour marque une notification comme non lue
    public void marquerCommeLue(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Notification introuvable"));
        notification.setLue(true); // Marquer comme lue
        notificationRepository.save(notification); // Mettre à jour dans la base
    }

    public void supprimerNotification(Long id) {
        notificationRepository.deleteById(id);
    }

}
