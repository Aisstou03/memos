package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ufr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    @JsonManagedReference
    @OneToMany(mappedBy = "ufr")
    private Set<Departement> departements;
    // Constructeur avec paramètre nom (ajouté manuellement)
    public Ufr(String nom) {
        this.nom = nom;
    }

    public Set<Departement> getDepartements() {
        return departements;
    }

    public void setDepartements(Set<Departement> departements) {
        this.departements = departements;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

