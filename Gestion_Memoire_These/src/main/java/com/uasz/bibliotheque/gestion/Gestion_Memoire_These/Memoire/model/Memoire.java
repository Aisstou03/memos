package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Memoire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID unique auto-généré
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
    @Enumerated(EnumType.STRING) // Sauvegarde le nom de l'enum en texte
    private TypeMemoire type; // Nouveau champ pour le type de mémoire
    @ManyToOne
    @JoinColumn(name = "filiere_id")
    @JsonBackReference
    private Filiere filiere;

    // Champs ajoutés pour les accès rapides à `Departement` et `Ufr`s
    @ManyToOne
    @JoinColumn(name = "departement_id")
    @JsonBackReference
    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "ufr_id")
    @JsonBackReference
    private Ufr ufr;

    @Column(name = "corbeille", nullable = false)
    private boolean corbeille = false; // Ajout du champ corbeille

    @ManyToMany
    @JoinTable(
            name = "memoire_mots_cles",
            joinColumns = @JoinColumn(name = "memoire_id"),
            inverseJoinColumns = @JoinColumn(name = "mot_cle_id")
    )
    private List<MotCle> motsCles = new ArrayList<>();

    @Column(name = "licencePro", nullable = false)
    private boolean licencePro = false; // Ajout du champ corbeille

    public boolean isLicencePro() {
        return licencePro;
    }

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

    public TypeMemoire getType() {
        return type;
    }

    public void setType(TypeMemoire type) {
        this.type = type;
    }

    public Filiere getFiliere() {
        return filiere;
    }

    public void setFiliere(Filiere filiere) {
        this.filiere = filiere;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Ufr getUfr() {
        return ufr;
    }

    public void setUfr(Ufr ufr) {
        this.ufr = ufr;
    }

    public boolean isCorbeille() {
        return corbeille;
    }

    public void setCorbeille(boolean corbeille) {
        this.corbeille = corbeille;
    }

    public List<MotCle> getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(List<MotCle> motsCles) {
        this.motsCles = motsCles;
    }

    public void setLicencePro(boolean licencePro) {
        this.licencePro = licencePro;
    }
}
