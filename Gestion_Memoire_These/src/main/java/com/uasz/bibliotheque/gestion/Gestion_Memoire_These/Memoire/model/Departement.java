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

    public Ufr getUfr() {
        return ufr;
    }

    public void setUfr(Ufr ufr) {
        this.ufr = ufr;
    }

    public Set<Filiere> getFilieres() {
        return filieres;
    }

    public void setFilieres(Set<Filiere> filieres) {
        this.filieres = filieres;
    }
}
