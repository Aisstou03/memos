package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.EcoleDoctorat;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Encadrant;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Etudiant;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.EncadrantService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.EtudiantService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.TheseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EcoleDoctoratController {

    @Autowired
    private TheseRepository theseRepository;

    @Autowired
    EtudiantService etudiantService;

    @Autowired
    EncadrantService encadrantService;

    @Autowired
    private TheseService theseService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EncadrantRepository encadrantRepository;

    @Autowired
    private EcoleDoctoraleRepository ecoleDoctoraleRepository;
    private String extractShortName(String fullName) {
        int start = fullName.indexOf("(");
        int end = fullName.indexOf(")");

        if (start != -1 && end != -1 && start < end) {
            return fullName.substring(start + 1, end); // Récupère uniquement le texte entre parenthèses
        }

        return fullName; // Si pas de parenthèses, retourne le nom complet
    }


    private String generateCote(String nomEcole, int annee, int exemplaires) {
        // Récupérer les deux derniers chiffres de l'année (2025 → 25)
        String anneeShort = String.valueOf(annee).substring(2);

        // Générer la cote au format "ED-STI/25/1"
        return nomEcole + "/" + anneeShort + "/" + exemplaires;
    }


    @PostMapping("/theses/ajouter")
    public String addThesis(@RequestParam String titre,
                            @RequestParam String etudiantNom,
                            @RequestParam String etudiantPrenom,
                            @RequestParam String encadrantNom,
                            @RequestParam String encadrantPrenom,
                            @RequestParam int annee,
                            @RequestParam int exemplaires,
                            @RequestParam String ecoleDoctorale,
                            Model model) {
        try {
            // Vérification des champs vides
            if (titre.isBlank() || etudiantNom.isBlank() || etudiantPrenom.isBlank()
                    || encadrantNom.isBlank() || encadrantPrenom.isBlank()) {
                model.addAttribute("error", "Tous les champs doivent être remplis !");
                return "ajouterThese";
            }

            // Gestion de l'étudiant
            Etudiant etudiant = etudiantRepository.findByNomAndPrenom(etudiantNom, etudiantPrenom)
                    .orElseGet(() -> etudiantRepository.save(new Etudiant(null, etudiantNom, etudiantPrenom)));

            // Gestion de l'encadrant
            Encadrant encadrant = encadrantRepository.findByNomAndPrenom(encadrantNom, encadrantPrenom)
                    .orElseGet(() -> {
                        Encadrant newEncadrant = new Encadrant();
                        newEncadrant.setNom(encadrantNom);
                        newEncadrant.setPrenom(encadrantPrenom);
                        return encadrantRepository.save(newEncadrant);
                    });

            // Vérification de l'école doctorale
            Optional<EcoleDoctorat> ecoleDoctoraleOpt = ecoleDoctoraleRepository.findByNom(ecoleDoctorale);
            if (ecoleDoctoraleOpt.isEmpty()) {
                model.addAttribute("error", "L'école doctorale sélectionnée est invalide.");
                return "ajouterThese";
            }

            // Récupération de l'école doctorale
            EcoleDoctorat ecoleDoctoratEntity = ecoleDoctoraleOpt.get();

            // Extraire uniquement l'abréviation de l'école doctorale
            String shortName = extractShortName(ecoleDoctoratEntity.getNom());

            // Générer la cote en utilisant l'abréviation
            String coteGeneree = generateCote(shortName, annee, exemplaires);

            // Création et sauvegarde de la thèse
            These these = new These();
            these.setCote(coteGeneree);  // Cote générée automatiquement
            these.setTitre(titre);
            these.setEtudiant(etudiant);
            these.setEncadrant(encadrant);
            these.setAnnee(annee);
            these.setExemplaires(exemplaires);
            these.setEcoleDoctorat(ecoleDoctoratEntity);

            theseRepository.save(these);

            return "redirect:/memoires/doctorats";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout de la thèse : " + e.getMessage());
            return "ajouterThese";
        }
    }

    //listess des these
    @GetMapping("/memoires/doctorats")
    public String afficherTheses(Model model) {
        List<These> theses = theseService.getAllThese();
        model.addAttribute("theses", theses);
        return "doctorat"; // Renvoie vers la page HTML theses.html
    }

    @PostMapping("/filtre/these")
    public String afficherThesesParUFR(@RequestParam String ufrNom, Model model) {
        // Filtrer les thèses par UFR
        List<These> thesesFiltrees = theseRepository.findByEcoleDoctoratUfrNom(ufrNom);

        // Organiser les thèses par UFR et par École Doctorale
        Map<String, Map<String, List<These>>> thesesParUFRAndEcoleDoctorale = new HashMap<>();

        for (These these : thesesFiltrees) {
            String ufr = these.getEcoleDoctorat() != null ? these.getEcoleDoctorat().getUfr().getNom() : "Non spécifié";
            String ecoleDoctorale = these.getEcoleDoctorat() != null ? these.getEcoleDoctorat().getNom() : null;

            // Si l'école doctorale est non nulle, on organise par UFR et École Doctorale
            if (ecoleDoctorale != null) {
                thesesParUFRAndEcoleDoctorale
                        .computeIfAbsent(ufr, k -> new HashMap<>())
                        .computeIfAbsent(ecoleDoctorale, k -> new ArrayList<>())
                        .add(these);
            } else {
                // Si l'école doctorale est nulle, on l'ajoute à un groupe "Sans école doctorale"
                thesesParUFRAndEcoleDoctorale
                        .computeIfAbsent(ufr, k -> new HashMap<>())
                        .computeIfAbsent("Sans école doctorale", k -> new ArrayList<>())
                        .add(these);
            }
        }

        // Ajouter la liste des thèses groupées par UFR et École Doctorale au modèle
        model.addAttribute("thesesParUFRAndEcole", thesesParUFRAndEcoleDoctorale);

        return "doctorat";  // Vue qui affichera les thèses filtrées
    }


    // Méthode pour afficher la page de modification de la thèse
    @GetMapping("/theses/modifier/{id}")
    public String editThesis(@PathVariable("id") Long thesisId, Model model) {
        These these = theseService.getThesisById(thesisId);
        if (these == null) {
            return "redirect:/theses"; // Redirige si la thèse n'existe pas
        }

        List<Etudiant> etudiants = etudiantService.findAll();
        List<Encadrant> encadrants = encadrantService.findAll();

        model.addAttribute("these", these);
        model.addAttribute("etudiants", etudiants);
        model.addAttribute("encadrants", encadrants);

        return "modifierThese"; // Retourne la page HTML
    }

    // Traitement du formulaire de modification
    @PostMapping("/theses/modifier/{id}")
    public String modifierThesis(@PathVariable Long id, @ModelAttribute These updatedThesis) {
        theseService.updateThesis(id, updatedThesis);
        return "redirect:/theses/liste"; // Redirige après modification
    }

    // Méthode pour supprimer une thèse
    @GetMapping("/theses/supprimer/{id}")
    public String deleteThesis(@PathVariable("id") Long thesisId, RedirectAttributes redirectAttributes) {
        theseService.deleteThesis(thesisId);
        redirectAttributes.addFlashAttribute("successMessage", "Suppression réussie !");
        return "redirect:/memoires/liste"; // Redirige vers la liste des thèses après la suppression
    }

}
