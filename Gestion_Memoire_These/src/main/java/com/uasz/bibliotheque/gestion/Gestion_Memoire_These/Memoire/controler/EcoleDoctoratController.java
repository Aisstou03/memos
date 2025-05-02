package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class EcoleDoctoratController {
    @Autowired
    private NotificationService notificationService ;

    @Autowired
    private TheseRepository theseRepository;
    @Autowired
    private UfrService ufrService;
    @Autowired
    EtudiantService etudiantService;

    @Autowired
    MotCleRepository motCleRepository;

    @Autowired
    EncadrantService encadrantService;

    @Autowired
    private MemoireService memoireService;

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
    public String addThesis(   @RequestParam("titre") String titre,
                               @RequestParam("annee") int annee,
                               @RequestParam("exemplaires") int exemplaires,
                               @RequestParam("etudiantNom") String etudiantNom,
                               @RequestParam("encadrantNom") String encadrantNom,
                               @RequestParam("motsCles") String motsCles,  // Chaîne de mots-clés séparée par des virgules
                             @RequestParam String ecoleDoctorale,
                            Model model) {
        try {
            // Vérification des champs vides
            if (titre.isBlank() || etudiantNom.isBlank() || encadrantNom.isBlank()) {
                model.addAttribute("error", "Tous les champs doivent être remplis !");
                return "ajoutThese";
            }

            // Transformation de la chaîne de mots-clés en liste
            List<String> motsClesList = Arrays.asList(motsCles.split("\\s*,\\s*"));  // Séparation sur les virgules + nettoyage des espaces

            // Gestion de l'étudiant
            Etudiant etudiant = etudiantRepository.findByNom(etudiantNom)
                    .orElseGet(() -> etudiantRepository.save(new Etudiant(null, etudiantNom)));

            // Gestion de l'encadrant
            Encadrant encadrant = encadrantRepository.findByNom(encadrantNom)
                    .orElseGet(() -> {
                        Encadrant newEncadrant = new Encadrant();
                        newEncadrant.setNom(encadrantNom);
                        return encadrantRepository.save(newEncadrant);
                    });

            // Vérification de l'école doctorale
            Optional<EcoleDoctorat> ecoleDoctoraleOpt = ecoleDoctoraleRepository.findByNom(ecoleDoctorale);
            if (ecoleDoctoraleOpt.isEmpty()) {
                model.addAttribute("error", "L'école doctorale sélectionnée est invalide.");
                return "ajoutThese";
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
            List<MotCle> motsClesEntities = new ArrayList<>();
            for (String mot : motsClesList) {
                String motNettoye = mot.trim().toLowerCase();
                MotCle motCle = motCleRepository.findByValeur(motNettoye)
                        .orElseGet(() -> motCleRepository.save(new MotCle(motNettoye)));
                motsClesEntities.add(motCle);
            }

            these.setMotsCles(motsClesEntities);

            theseRepository.save(these);

            return "redirect:/memoires/doctorats";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout de la thèse : " + e.getMessage());
            return "ajoutThese";
        }
    }

    //liste
    @GetMapping("/memoires/doctorats")
    public String afficherToutesLesTheses(Model model, Principal principal,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Récupérer toutes les mémoires de type These
        Page<These> theses = theseService.getAllThese(pageable);
        model.addAttribute("theses", theses);
        model.addAttribute("rechercheEffectuee", false); // Ajouté pour bien différencier les cas

        // Gestion de l'utilisateur connecté
        if (principal != null) {
            Utilisateur utilisateur = memoireService.recherche_Utilisateur(principal.getName());
            if (utilisateur != null) {
                // Ajouter les informations de l'utilisateur au modèle
                model.addAttribute("nom", utilisateur.getNom());
                model.addAttribute("prenom", utilisateur.getPrenom());

                // Extraire les rôles et les ajouter
                String roles = utilisateur.getRoles().stream()
                        .map(Role::getRole)
                        .reduce((role1, role2) -> role1 + ", " + role2)
                        .orElse("Aucun rôle");
                model.addAttribute("roles", roles);
            }
        }
        model.addAttribute("currentUser", principal.getName()); // Ajouter l'utilisateur actuel

        return "doctorat";
    }

    @PostMapping("/filtre/these")
    public String afficherThesesParUFR(@RequestParam String ufrNom,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       Model model) {
        // Activer le mode filtrage
        model.addAttribute("isFiltered", true);
        Pageable pageable = PageRequest.of(page, size);

        // Filtrer les thèses par UFR
        Page<These> thesesFiltrees = theseRepository.findByEcoleDoctoratUfrNom(ufrNom, pageable);

        // Organiser les thèses par UFR et par École Doctorale
        Map<String, Map<String, List<These>>> thesesParUFRAndEcoleDoctorale = new HashMap<>();

        for (These these : thesesFiltrees) {
            String ufr = these.getEcoleDoctorat() != null ? these.getEcoleDoctorat().getUfr().getNom() : "Non spécifié";
            String ecoleDoctorale = these.getEcoleDoctorat() != null ? these.getEcoleDoctorat().getNom() : null;

            if (ecoleDoctorale != null) {
                thesesParUFRAndEcoleDoctorale
                        .computeIfAbsent(ufr, k -> new HashMap<>())
                        .computeIfAbsent(ecoleDoctorale, k -> new ArrayList<>())
                        .add(these);
            } else {
                thesesParUFRAndEcoleDoctorale
                        .computeIfAbsent(ufr, k -> new HashMap<>())
                        .computeIfAbsent("Sans école doctorale", k -> new ArrayList<>())
                        .add(these);
            }
        }

        // Ajouter la liste des thèses groupées par UFR et École Doctorale au modèle
        model.addAttribute("thesesParUFRAndEcole", thesesParUFRAndEcoleDoctorale);
        model.addAttribute("selectedUFR", ufrNom); // Pour vérifier la sélection dans Thymeleaf
        model.addAttribute("pageTheses", thesesFiltrees);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", thesesFiltrees.getTotalPages());
        model.addAttribute("pageSize", size);

        return "doctorat";  // Vue qui affichera les thèses filtrées
    }

    /**
     * Affiche la page de modification avec ou sans recherche.
     */    @GetMapping("/modifier/theses")
    public String editThesis( Model model) {
        model.addAttribute("these", new These()); // these vide pour le formulaire
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("encadrants", encadrantService.findAll());
        model.addAttribute("ufrs", ufrService.findAllUfrs());
        return "modifierThese"; // Retourne la page HTML
    }

    // Méthode pour afficher la page de modification de la thèse choisi
    @GetMapping("/theses/modifier/{id}")
    public String editThesis(@PathVariable("id") Long thesisId, Model model) {
        These these = theseService.getThesisById(thesisId);

        if (these == null) {
            return "redirect:/memoires/doctorats";
        }

        System.out.println("Thèse ID: " + these.getId());
        System.out.println("Titre: " + these.getTitre());
        System.out.println("Côte: " + these.getCote());
        System.out.println("Année: " + these.getAnnee());
        System.out.println("Exemplaires: " + these.getExemplaires());
        if (these.getEcoleDoctorat() != null) {
            System.out.println("UFR: " + these.getEcoleDoctorat().getNom());
        } else {
            System.out.println("UFR est NULL !");
        }

        List<Ufr> ufrs = ufrService.findAllUfrs();
        List<Etudiant> etudiants = etudiantService.findAll();
        List<Encadrant> encadrants = encadrantService.findAll();

        model.addAttribute("these", these);
        model.addAttribute("etudiants", etudiants);
        model.addAttribute("encadrants", encadrants);
        model.addAttribute("ufrs", ufrs);

        return "modifierThese";
    }

    @PostMapping("/theses/modifier/{id}")
    public String modifierThese(@PathVariable("id") Long id, @ModelAttribute These these, RedirectAttributes redirectAttributes) {
        try {
            // Appel au service pour mettre à jour la thèse
            These updatedThesis = theseService.updateThesis(id, these);

            // Ajout du message de succès et redirection vers la liste des mémoires
            redirectAttributes.addFlashAttribute("successMessage", "Thèse mise à jour avec succès !");
            return "redirect:/memoires/doctorats";
        } catch (IllegalArgumentException e) {
            // Gestion des erreurs spécifiques (par exemple, étudiant ou encadrant introuvable)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/theses/modifier/" + id;
        } catch (Exception e) {
            // Gestion des erreurs générales (par exemple, erreurs système)
            redirectAttributes.addFlashAttribute("errorMessage", "Une erreur est survenue lors de la mise à jour.");
            return "redirect:/theses/modifier/" + id;
        }
    }

    // Méthode pour supprimer une thèse
    @PostMapping("/theses/supprimer")
    public String supprimerThese(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        These these = theseRepository.findById(id).orElse(null);
        if (these != null) {
            // Marquer la thèse comme supprimée (déplacée dans la corbeille)
            these.setCorbeille(true);
            these.setEstSupprime(true);
            theseRepository.save(these);  // Sauvegarder l'état modifié de la thèse
            redirectAttributes.addFlashAttribute("successMessage", "Thèse déplacée dans la corbeille !");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Thèse introuvable !");
        }
        return "redirect:/memoires/doctorats"; //  la redirection
    }


    @PostMapping("/theses/restaurer")
    public String restaurerThese(@RequestParam("theseId") Long theseId, RedirectAttributes redirectAttributes) {
        These these = theseRepository.findById(theseId).orElse(null);
        if (these != null) {
            // Restauration de la thèse
            these.setEstSupprime(false);
            these.setCorbeille(false);
            theseRepository.save(these); // Sauvegarder la thèse restaurée
            redirectAttributes.addFlashAttribute("successMessage", "Thèse restauré avec succès !");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Thèse introuvable !");
        }
        return "redirect:/memoires/corbeille"; // Rediriger vers la corbeille après la restauration
    }

    @PostMapping("/theses/supprimerDefinitivement")
    public String supprimerDefinitivement(@RequestParam("id") Long theseId, RedirectAttributes redirectAttributes) {
        These these = theseRepository.findById(theseId).orElse(null);
        if (these != null) {
            theseRepository.delete(these);
            redirectAttributes.addFlashAttribute("successMessage", "Thèse supprimé définitivement !");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer une Thèse qui n'est pas en corbeille.");
        }
        return "redirect:/memoires/corbeille"; // Correction de la redirection
    }

    /**
     * Generation dattestation
     */
    @GetMapping("/theses/genererAttestation/{id}")
    public String detailsThese(@PathVariable Long id, Model model) {
        These these = theseService.getThesisById(id);
        model.addAttribute("these", these);
        return "GenererAttestationThese"; // page de generation des attestations
    }
}