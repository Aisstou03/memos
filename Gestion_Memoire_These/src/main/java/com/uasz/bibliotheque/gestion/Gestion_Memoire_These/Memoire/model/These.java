package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    // Dans l'entité These, modifiez le @JoinTable :
    @ManyToMany
    @JoinTable(
            name = "these_mots_cles",  // Table différente
            joinColumns = @JoinColumn(name = "these_id"),
            inverseJoinColumns = @JoinColumn(name = "mot_cle_id")
    )
    private List<MotCle> motsCles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCote() {
        return cote;
    }

    public void setCote(String cote) {
        this.cote = cote;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Encadrant getEncadrant() {
        return encadrant;
    }

    public void setEncadrant(Encadrant encadrant) {
        this.encadrant = encadrant;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getExemplaires() {
        return exemplaires;
    }

    public void setExemplaires(int exemplaires) {
        this.exemplaires = exemplaires;
    }

    public boolean isCorbeille() {
        return corbeille;
    }

    public void setCorbeille(boolean corbeille) {
        this.corbeille = corbeille;
    }

    public EcoleDoctorat getEcoleDoctorat() {
        return ecoleDoctorat;
    }

    public void setEcoleDoctorat(EcoleDoctorat ecoleDoctorat) {
        this.ecoleDoctorat = ecoleDoctorat;
    }

    public Boolean getEstSupprime() {
        return estSupprime;
    }

    public void setEstSupprime(Boolean estSupprime) {
        this.estSupprime = estSupprime;
    }

    public List<MotCle> getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(List<MotCle> motsCles) {
        this.motsCles = motsCles;
    }
}