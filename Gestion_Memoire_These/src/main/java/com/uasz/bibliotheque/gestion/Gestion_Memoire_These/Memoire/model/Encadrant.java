package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Encadrant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;

    @ManyToOne
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;  // Ajout de la relation avec Filiere

    // Méthode pour obtenir le nom complet
    public String getNomComplet() {
        return this.nom + " " + this.prenom;
    }
}
