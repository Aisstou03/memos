package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    @JsonBackReference // Empêche la sérialisation de l'UFR dans Departement
    @ManyToOne
    @JoinColumn(name = "ufr_id")
    private Ufr ufr;

    @OneToMany(mappedBy = "departement")
    private Set<Filiere> filieres;

    // Constructeur avec paramètre nom et ufr (ajouté manuellement)
    public Departement(String nom, Ufr ufr) {
        this.nom = nom;
        this.ufr = ufr;
    }
}
