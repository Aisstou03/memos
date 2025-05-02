package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.TheseSpecifications;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TheseService {
    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EncadrantService encadrantService;

    @Autowired
    private UfrService ufrService;

    private final TheseRepository theseRepository;
    private final EtudiantRepository etudiantRepository;
    private final EncadrantRepository encadrantRepository;
    private final EcoleDoctoraleRepository ecoleDoctoraleRepository;
    private final MotCleRepository motCleRepository;

    public TheseService(TheseRepository theseRepository,
                        EtudiantRepository etudiantRepository,
                        EncadrantRepository encadrantRepository,
                        EcoleDoctoraleRepository ecoleDoctoraleRepository,
                        MotCleRepository motCleRepository) {
        this.theseRepository = theseRepository;
        this.etudiantRepository = etudiantRepository;
        this.encadrantRepository = encadrantRepository;
        this.ecoleDoctoraleRepository = ecoleDoctoraleRepository;
        this.motCleRepository = motCleRepository;
    }

    @Autowired
    private NotificationService notificationService;


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
        Etudiant etudiant = null;
        if (thesemodifiee.getEtudiant() != null && thesemodifiee.getEtudiant().getId() != null) {
             etudiant = etudiantService.findById(thesemodifiee.getEtudiant().getId());
        }
        else {
            // Sinon, créer un nouvel étudiant avec les informations fournies
            etudiant = etudiantService.createNewStudent(thesemodifiee.getEtudiant());
        }

        // Mise à jour de l'encadrant
        Encadrant encadrant = null;
        if (thesemodifiee.getEncadrant() != null && thesemodifiee.getEncadrant().getId() != null) {
            encadrant = encadrantService.findById(thesemodifiee.getEncadrant().getId());
        } else {
        // Sinon, créer un nouvel encadrant avec les informations fournies
        encadrant = encadrantService.createNewSupervisor(thesemodifiee.getEncadrant());
    }

        // Mise à jour de l'année et des exemplaires
        theseExistante.setAnnee(thesemodifiee.getAnnee());
        theseExistante.setExemplaires(thesemodifiee.getExemplaires());
        theseExistante.setEtudiant(etudiant);
        theseExistante.setEncadrant(encadrant);
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

    public void ajouterThese(String titre, int annee, int exemplaires, String etudiantNom, String encadrantNom,
                             String motsCles, String nomEcoleDoctorale) throws Exception {

        if (titre.isBlank() || etudiantNom.isBlank() || encadrantNom.isBlank()) {
            throw new IllegalArgumentException("Tous les champs doivent être remplis !");
        }

        // Etudiant
        Etudiant etudiant = etudiantRepository.findByNom(etudiantNom)
                .stream().findFirst()
                .orElseGet(() -> etudiantRepository.save(new Etudiant(null, etudiantNom)));

        // Encadrant
        Encadrant encadrant = encadrantRepository.findByNom(encadrantNom)
                .stream().findFirst()
                .orElseGet(() -> encadrantRepository.save(new Encadrant(null, encadrantNom)));

        // École doctorale
        EcoleDoctorat ecoleDoctorat = ecoleDoctoraleRepository.findByNom(nomEcoleDoctorale)
                .stream().findFirst()
                .orElseThrow(() -> new Exception("L'école doctorale est invalide."));

        // Cote
        String cote = generateCote(ecoleDoctorat.getNom(), annee, exemplaires);

        // Mots-clés
        List<MotCle> motsCleEntities = Arrays.stream(motsCles.split("\\s*,\\s*"))
                .map(String::toLowerCase)
                .map(String::trim)
                .distinct()
                .map(val -> motCleRepository.findByValeur(val).orElseGet(() -> motCleRepository.save(new MotCle(val))))
                .collect(Collectors.toList());

        // Thèse
        These these = new These();
        these.setTitre(titre);
        these.setAnnee(annee);
        these.setExemplaires(exemplaires);
        these.setCote(cote);
        these.setEtudiant(etudiant);
        these.setEncadrant(encadrant);
        these.setEcoleDoctorat(ecoleDoctorat);
        these.setMotsCles(motsCleEntities);

        theseRepository.save(these);
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


    public Page<These> getAllThese(Pageable pageable) {
        return theseRepository.findAllNotDeleted(pageable); // Ne récupère que les thèses non supprimées
    }


    public List<These> searchMemos(Specification<These> spec) {
        return theseRepository.findAll(spec);
    }


}