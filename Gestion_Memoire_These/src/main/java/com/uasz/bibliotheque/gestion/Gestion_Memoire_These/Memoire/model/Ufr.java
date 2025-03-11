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
    }}

