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

    @ManyToMany(mappedBy = "motsCles")
    private List<These> theses = new ArrayList<>();
    // Constructeur personnalisé
    public MotCle(String valeur) {
        this.valeur = valeur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public List<Memoire> getMemoires() {
        return memoires;
    }

    public void setMemoires(List<Memoire> memoires) {
        this.memoires = memoires;
    }

    public List<These> getTheses() {
        return theses;
    }

    public void setTheses(List<These> theses) {
        this.theses = theses;
    }
}
