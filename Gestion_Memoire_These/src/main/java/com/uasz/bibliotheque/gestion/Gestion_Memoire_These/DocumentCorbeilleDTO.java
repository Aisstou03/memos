package com.uasz.bibliotheque.gestion.Gestion_Memoire_These;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;

public class DocumentCorbeilleDTO {
    private Long id;
    private String titre;
    private String etudiantNom;
    private String encadrantNom;
    private String type; // "LICENCE", "MASTER" ou "THESE"

    public DocumentCorbeilleDTO(Memoire memoire) {
        this.id = memoire.getId();
        this.titre = memoire.getTitre();
        this.etudiantNom = memoire.getEtudiant().getNom();
        this.encadrantNom = memoire.getEncadrant().getNom();
        this.type = memoire.getType().name(); // LICENCE ou MASTER
    }

    public DocumentCorbeilleDTO(These these) {
        this.id = these.getId();
        this.titre = these.getTitre();
        this.etudiantNom = these.getEtudiant().getNom();
        this.encadrantNom = these.getEncadrant().getNom();
        this.type = "THESE";
    }

}

