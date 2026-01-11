package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Directeur {

    @Id
    private Long id = 1L; // On en aura toujours qu’un

    private String nom;

    public Directeur() {}

    public Directeur(String nom) {
        this.nom = nom;
    }

    // getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
