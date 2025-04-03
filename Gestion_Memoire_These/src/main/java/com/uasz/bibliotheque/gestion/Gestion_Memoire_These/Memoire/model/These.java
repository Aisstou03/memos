package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class These {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cote", nullable = false)
    private String cote;
    private String titre;

    @ManyToOne
    @JoinColumn(name = "etudiant_id")
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "encadrant_id")
    private Encadrant encadrant;
    private int annee;
    private int exemplaires;  // Le nombre d'exemplaires

    @Column(name = "corbeille", nullable = false)
    private boolean corbeille = false; // Ajout du champ corbeille

    @JsonBackReference // Empêche la sérialisation de l'UFR dans Departement
    @ManyToOne
    @JoinColumn(name = "ecoleDoctorat_id")
    private EcoleDoctorat ecoleDoctorat;

    @Column(name = "est_supprime")
    private Boolean estSupprime = false;  // Champ pour indiquer si la thèse est dans la corbeille

}
