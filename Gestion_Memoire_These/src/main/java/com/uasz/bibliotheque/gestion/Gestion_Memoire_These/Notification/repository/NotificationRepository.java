package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.repository;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByLueFalse(); // Récupérer les notifications non lues
}
