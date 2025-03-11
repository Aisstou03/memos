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
public class EcoleDoctorat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'DOCTORAT'")
    private TypeMemoire type = TypeMemoire.DOCTORAT;

    @JsonBackReference // Empêche la sérialisation de l'UFR dans Departement
    @ManyToOne
    @JoinColumn(name = "ufr_id")
    private Ufr ufr;

    // Constructeur avec paramètre nom et ufr (ajouté manuellement)
    public EcoleDoctorat(String nom, Ufr ufr) {
        this.nom = nom;
        this.ufr = ufr;
    }
}
