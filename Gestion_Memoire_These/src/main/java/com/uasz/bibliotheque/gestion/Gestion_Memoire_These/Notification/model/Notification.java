package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private LocalDateTime dateCreation;

    @Column(name = "vue") // Si la colonne en DB s'appelle 'vue'
    private boolean lue = false; // Valeur par défaut définie ici // Par défaut false pour une nouvelle notification

    public Notification() {}

    public Notification(String message) {
        this.message = message;
        this.dateCreation = LocalDateTime.now(); // Initialise avec la date actuelle
        this.lue = false; // Par défaut, la notification n'est pas lue
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isLue() {
        return lue;
    }

    public void setLue(boolean lue) {
        this.lue = lue;
    }
}
