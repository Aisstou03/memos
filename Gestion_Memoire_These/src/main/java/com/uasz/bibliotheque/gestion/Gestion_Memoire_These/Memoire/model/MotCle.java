package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotCle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String valeur;

    @ManyToMany(mappedBy = "motsCles")
    private List<Memoire> memoires = new ArrayList<>();
    // Constructeur personnalisé
    public MotCle(String valeur) {
        this.valeur = valeur;
    }
}
