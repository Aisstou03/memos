package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.TheseRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.TheseSpecifications;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TheseService {
    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EncadrantService encadrantService;

    @Autowired
    private UfrService ufrService;

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private TheseRepository theseRepository;
    // Méthode pour récupérer une thèse par son ID
    public These getThesisById(Long id) {
        return theseRepository.findById(id).orElse(null); // Retourne la thèse si elle existe, sinon null
    }

    public These updateThesis(Long id, These thesemodifiee) {
        // Vérifie si la thèse existe
        These theseExistante = theseRepository.findById(id).orElse(null);
        if (theseExistante == null) {
            throw new IllegalArgumentException("Thèse avec ID " + id + " n'existe pas.");
        }

        // Mise à jour des champs basiques
        theseExistante.setTitre(thesemodifiee.getTitre());

        // Mise à jour de l'étudiant
        if (thesemodifiee.getEtudiant() != null && thesemodifiee.getEtudiant().getId() != null) {
            Etudiant etudiant = etudiantService.findById(thesemodifiee.getEtudiant().getId());
            if (etudiant != null) {
                theseExistante.setEtudiant(etudiant);
            }
        }

        // Mise à jour de l'encadrant
        if (thesemodifiee.getEncadrant() != null && thesemodifiee.getEncadrant().getId() != null) {
            Encadrant encadrant = encadrantService.findById(thesemodifiee.getEncadrant().getId());
            if (encadrant != null) {
                theseExistante.setEncadrant(encadrant);
            }
        }

        // Mise à jour de l'année et des exemplaires
        theseExistante.setAnnee(thesemodifiee.getAnnee());
        theseExistante.setExemplaires(thesemodifiee.getExemplaires());

        // Génération de la nouvelle cote (en gardant l'école doctorale existante)
        String nomEcole = extractSchoolName(theseExistante.getCote());
        String nouvelleCote = generateCote(nomEcole, thesemodifiee.getAnnee(), thesemodifiee.getExemplaires());
        theseExistante.setCote(nouvelleCote);

        // Sauvegarde de la thèse mise à jour
        These theseSauvegardee = theseRepository.save(theseExistante);

        // Création de la notification
        String messageNotification = "La thèse \"" + theseSauvegardee.getTitre() + "\" a été modifiée.";
        notificationService.creerNotification(messageNotification);
        System.out.println("Notification créée : " + messageNotification);

        return theseSauvegardee;
    }

    // Extraction de l'école doctorale depuis la cote existante
    public String extractSchoolName(String cote) {
        if (cote != null && cote.contains("/")) {
            return cote.split("/")[0]; // Prend la partie avant le premier "/"
        }
        return "Inconnu"; // Si la cote est mal formée
    }

    // Génération de la cote avec l'école existante
    private String generateCote(String nomEcole, int annee, int exemplaires) {
        String anneeShort = String.valueOf(annee).substring(2); // Extrait les deux derniers chiffres
        return nomEcole + "/" + anneeShort + "/" + exemplaires;
    }


    public List<These> getAllThese() {
        return theseRepository.findAllNotDeleted(); // Ne récupère que les thèses non supprimées
    }


    public List<These> searchMemos(Specification<These> spec) {
        return theseRepository.findAll(spec);
    }


}