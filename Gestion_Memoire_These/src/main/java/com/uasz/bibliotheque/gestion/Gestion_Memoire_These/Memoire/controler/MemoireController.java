package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.UtilisateurService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.TheseRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.MemoireRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MemoireController {

    private static final Logger logger = LoggerFactory.getLogger(MemoireController.class);

    @Autowired
    private MemoireService memoireService;
    @Autowired
    private TheseRepository theseRepository;

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private MemoireRepository memoireRepository;



    @Autowired
    private TheseService theseService;
    @Autowired
    private NotificationService notificationService ;
    @Autowired
    EtudiantService etudiantService;
    @Autowired
    EncadrantService encadrantService;
    @Autowired
    FiliereService filiereService;
    @Autowired
    private UfrService ufrService;
    @Autowired
    private DirecteurService directeurService ;

    @Autowired
    private DepartementService departementService;
    @Autowired
    private StatistiquesService statistiquesService;

    //formulaire d'ajout Licence
    @RequestMapping(value = "/ajoutMemoire", method = RequestMethod.GET)
    public String showAjoutMemoireForm(Model model) {
        model.addAttribute("memoire", new Memoire()); // ou l'objet correspondant
        return "Gestion_ajoutes/ajoutMemoire";
    }

    //formulaire d'ajout Master
    @RequestMapping(value = "/ajoutMaster", method = RequestMethod.GET)
    public String afficherFormulaireMaster(Model model) {
        model.addAttribute("memoire", new Memoire()); // ou l'objet correspondant

        return "Gestion_ajoutes/ajoutMaster"; // Assurez-vous que `formulaireMaster.html` est dans `templates`.
    }

    //formulaire d'ajout These
    @RequestMapping(value = "/ajoutThese", method = RequestMethod.GET)
    public String formulaireLicence(Model model) {
        model.addAttribute("memoire", new Memoire()); // ou l'objet correspondant

        return "Gestion_ajoutes/ajoutThese"; // Nom du fichier Thymeleaf
    }


    @PostMapping("/memoires/ajouter")
    public String ajouterMemoire(
            @RequestParam("ufrNom") String ufrNom,
            @RequestParam("departementNom") String departementNom,
            @RequestParam("filiereNom") String filiereNom,
            @RequestParam("type") String type,
            @RequestParam("titre") String titre,
            @RequestParam("annee") int annee,
            @RequestParam("exemplaires") int exemplaires,
            @RequestParam("etudiantNom") String etudiantNom,        // Uniquement le nom
            @RequestParam("encadrantNom") String encadrantNom,      // Uniquement le nom
            @RequestParam("motsCles") String motsCles,
            @RequestParam(name = "licencePro", defaultValue = "false") boolean licencePro,
            RedirectAttributes redirectAttributes) {

        try {
            // Vérification des champs requis
            if (ufrNom.isEmpty() || departementNom.isEmpty() || filiereNom.isEmpty() ||
                    type.isEmpty() || titre.isEmpty() || annee <= 0 || exemplaires <= 0 ||
                    etudiantNom.isEmpty() || encadrantNom.isEmpty()) {

                redirectAttributes.addFlashAttribute("error", "Tous les champs sont requis !");
                return "redirect:/ajoutMemoire";  // Redirige vers le formulaire
            }

            // Transformation de la chaîne de mots-clés en liste
            List<String> motsClesList = Arrays.asList(motsCles.split("\\s*,\\s*"));

            // Appel du service - ADAPTEZ CETTE LIGNE À VOTRE SERVICE
            memoireService.ajouterMemoire(
                    ufrNom, departementNom, filiereNom, type, titre, annee, exemplaires,
                    etudiantNom, encadrantNom, motsClesList, licencePro
            );

            redirectAttributes.addFlashAttribute("message", "Mémoire ajouté avec succès !");
            return "redirect:/memoires/liste";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
            return "redirect:/ajoutMemoire";  // Redirige vers le formulaire en cas d'erreur
        }
    }

    public MemoireController(MemoireService memoireService, StatistiquesService statistiquesService) {
        this.memoireService = memoireService;
        this.statistiquesService = statistiquesService;
    }


    //liste de tous les memoires
    @RequestMapping(value = "/memoires/liste", method = RequestMethod.GET)
    public String afficherMemoires(Model model, Principal principal) {
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

        // Si aucun critère n'est fourni, récupérer tous les mémoires groupés
        Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoireService.getMemoiresGroupes();
        model.addAttribute("memoiresGroupes", memoiresGroupes);
        model.addAttribute("notifications", notificationService.getNotificationNonLue());
        model.addAttribute("currentUser", principal.getName()); // Ajouter l'utilisateur actuel
        long nombreUtilisateurs = utilisateurService.getNombreUtilisateurs();
        model.addAttribute("nombreUtilisateurs", nombreUtilisateurs);

        statistiquesService.ajouterStatistiques(model);

        return "tableau_de_bord/dashboard_Admin"; // Vue pour afficher les résultats de recherche
    }

    /**
     * Affiche le formulaire pré-rempli pour un mémoire donné.
     */
    @GetMapping("/modif/{id}")
    public String modif(@PathVariable Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        Memoire memoire = memoireService.getMemoireById(id);
        if (memoire == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Mémoire introuvable !");
            return "redirect:/memoires/liste";
        }
        model.addAttribute("memoire", memoire);
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("encadrants", encadrantService.findAll());
        model.addAttribute("filieres", filiereService.findAll());

        // Ajout du type pour retour conditionnel
        model.addAttribute("typeMemoire", memoire.getType().name()); // "LICENCE", "MASTER" ou "DOCTORAT"
        // Gestion de l'utilisateur connecté
        if (principal != null) {
            Utilisateur utilisateur = memoireService.recherche_Utilisateur(principal.getName());
            if (utilisateur != null) {
                model.addAttribute("nom", utilisateur.getNom());
                model.addAttribute("prenom", utilisateur.getPrenom());

                String roles = utilisateur.getRoles().stream()
                        .map(Role::getRole)
                        .reduce((role1, role2) -> role1 + ", " + role2)
                        .orElse("Aucun rôle");
                model.addAttribute("roles", roles);
            }
            model.addAttribute("currentUser", principal.getName());
        }
        return "modif";
    }


    /**
     * Recherche des mémoires en fonction des critères fournis.
     */ //Pour la modification
    @GetMapping("/modif/recherche")
    public String rechercherMemosModifier(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) Integer annee,
            Model model
    ) {
        // Rassembler les paramètres de recherche dans un Map
        Map<String, String> params = new HashMap<>();
        if (cote != null) params.put("cote", cote);
        if (titre != null) params.put("titre", titre);
        if (filiere != null) params.put("filiere", filiere);
        if (etudiant != null) params.put("etudiant", etudiant);
        if (encadrant != null) params.put("encadrant", encadrant);
        if (annee != null) params.put("annee", annee.toString());

        // Appel de la méthode de recherche
        List<Memoire> memoires = memoireService.rechercherMemos(params);

        // Si aucun mémoire n'est trouvé, afficher un message d'erreur
        if (memoires.isEmpty()) {
            model.addAttribute("message", "Aucun mémoire trouvé pour les critères spécifiés.");
        } else if (memoires.size() == 1) {
            // Si un seul mémoire est trouvé, pré-remplir les champs de modification
            model.addAttribute("memoire", memoires.get(0));
        } else {
            // Si plusieurs mémoires sont trouvés, afficher la liste pour que l'utilisateur puisse en choisir un
            model.addAttribute("memoires", memoires);
            model.addAttribute("message", "Plusieurs mémoires trouvés, veuillez en sélectionner un.");
        }

        // Ajouter les autres attributs nécessaires pour la modification (comme les listes d'étudiants, encadrants, etc.)
        model.addAttribute("etudiants", etudiantService.findAll());
        model.addAttribute("encadrants", encadrantService.findAll());
        model.addAttribute("filieres", filiereService.findAll());

        return "modif";
    }

    // Suppression vers la corbeille (Méthode POST au lieu de GET)
    @PostMapping("/memoires/supprimer")
    public String envoyerEnCorbeille(
            @RequestParam Long id,
            @RequestParam String type,
            RedirectAttributes redirectAttributes) {

        Optional<Memoire> memoireOpt = memoireRepository.findById(id);

        if (memoireOpt.isPresent()) {
            Memoire memoire = memoireOpt.get();
            memoire.setCorbeille(true);
            memoireRepository.save(memoire);

            String tab = type.equalsIgnoreCase("LICENCE") ? "licence" : "master";

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Mémoire déplacé dans la corbeille. " +
                            "<a href='/memoires/corbeille?tab=" + tab + "'>Voir la corbeille</a>"
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Mémoire introuvable !"
            );
        }

        return type.equalsIgnoreCase("LICENCE")
                ? "redirect:/licence"
                : "redirect:/master";
    }


    // Affichage de la corbeille
    @GetMapping("/memoires/corbeille")
    public String corbeille(Model model, Principal principal,
                            @RequestParam(defaultValue = "0") int pageLicence,
                            @RequestParam(defaultValue = "0") int pageMaster,
                            @RequestParam(defaultValue = "0") int pageThese,
                            @RequestParam(defaultValue = "10") int size) {

        Pageable pageableLicence = PageRequest.of(pageLicence, size);
        Pageable pageableMaster = PageRequest.of(pageMaster, size);
        Pageable pageableThese = PageRequest.of(pageThese, size);


        Page<Memoire> pageMemoiresLicence = memoireService.getMemoiresSupprimesLicence(pageableLicence);
        Page<Memoire> pageMemoiresMaster = memoireService.getMemoiresSupprimesMaster(pageableMaster);
        Page<These> thesesDansCorbeille = theseRepository.findByEstSupprime(true, pageableThese);

        model.addAttribute("pageMemoiresLicence", pageMemoiresLicence);
        model.addAttribute("pageMemoiresMaster", pageMemoiresMaster);
        model.addAttribute("memoiresLicence", pageMemoiresLicence.getContent());
        model.addAttribute("memoiresMaster", pageMemoiresMaster.getContent());
        model.addAttribute("memoiresMaster", pageMemoiresMaster.getContent());
        model.addAttribute("thesesDansCorbeille", thesesDansCorbeille.getContent()); // c’est la liste à afficher
        model.addAttribute("pagesThese", thesesDansCorbeille); // c’est l’objet Page complet (pagination)

        // Gestion de l'utilisateur connecté
        if (principal != null) {
            Utilisateur utilisateur = memoireService.recherche_Utilisateur(principal.getName());
            if (utilisateur != null) {
                model.addAttribute("nom", utilisateur.getNom());
                model.addAttribute("prenom", utilisateur.getPrenom());

                String roles = utilisateur.getRoles().stream()
                        .map(Role::getRole)
                        .reduce((role1, role2) -> role1 + ", " + role2)
                        .orElse("Aucun rôle");
                model.addAttribute("roles", roles);
            }
            model.addAttribute("currentUser", principal.getName());
        }

        return "widget/Corbeille";
    }


    @PostMapping("/memoires/restaurer")
    public String restaurerMemoire(
            @RequestParam Long id,
            @RequestParam String type,
            RedirectAttributes redirectAttributes) {

        Optional<Memoire> memoireOpt = memoireRepository.findById(id);

        if (memoireOpt.isPresent()) {
            Memoire memoire = memoireOpt.get();
            memoire.setCorbeille(false);
            memoireRepository.save(memoire);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Restauration effectuée avec succès !"
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Élément introuvable !"
            );
        }

        // 🔥 redirection vers le bon onglet
        return "redirect:/memoires/corbeille?tab=" + type;
    }


    @PostMapping("/memoires/supprimer-definitivement")
    public String supprimerDefinitivement(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        Optional<Memoire> memoireOpt = memoireRepository.findById(id);
        if (memoireOpt.isPresent()) {
            Memoire memoire = memoireOpt.get();
            if (memoire.isCorbeille()) { // Vérifie si le mémoire est bien dans la corbeille avant suppression
                memoireRepository.delete(memoire);
                redirectAttributes.addFlashAttribute("successMessage", "Mémoire supprimé définitivement !");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer un mémoire qui n'est pas en corbeille.");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Mémoire introuvable !");
        }
        return "redirect:/memoires/corbeille";
    }


    @PostMapping("/memoires/modifier")
    public String modifierMemoire(
            @ModelAttribute Memoire memoire,
            RedirectAttributes redirectAttributes) {

        try {
            Memoire memoireModifiee = memoireService.modifierMemoire(memoire.getId(), memoire);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Mémoire mis à jour avec succès !");

            // 🔑 ID du mémoire modifié
            redirectAttributes.addFlashAttribute("updatedMemoireId",
                    memoireModifiee.getId());

            // 🔑 Type du mémoire (LICENCE / MASTER)
            redirectAttributes.addFlashAttribute("typeMemoire",
                    memoireModifiee.getType().name());

            // 🔁 Redirection selon le type
            if (memoireModifiee.getType() == TypeMemoire.LICENCE) {
                return "redirect:/licence";
            } else {
                return "redirect:/master";
            }

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/modif/" + memoire.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur inattendue.");
            return "redirect:/modif/" + memoire.getId();
        }
    }



    @GetMapping("/rechercheParAnnee")
    public String rechercheParAnnee(
            @RequestParam int annee,
            @RequestParam TypeMemoire type,
            @RequestParam(required = false) String ufr,
            @RequestParam(required = false) String departement,
            Model model) {

        List<Memoire> resultats = memoireService.rechercheAvancee(
                annee, type, ufr, departement
        );

        model.addAttribute("resultats", resultats);
        model.addAttribute("anneeRecherchee", annee);
        model.addAttribute("typeRecherche", type);


        if (resultats.isEmpty()) {
            model.addAttribute("message",
                    "Aucun mémoire trouvé pour l'année " + annee + " et le type " + type);
        }

        return "telechargement/resultatsRecherche";
    }



    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String voir(Model model) {
        return "index"; // Assurez-vous que `formulaireMaster.html` est dans `templates`.
    }

    /**
     * Affiche le formulaire de filtrage des mémoires de Licence.
     */
    @GetMapping("/licence")
    public String afficherToutesLesMemoires(Model model, Principal principal,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Memoire> pageMemoires = memoireService.findMemoiresActifs(pageable);

        model.addAttribute("pageMemoires", pageMemoires);
        model.addAttribute("memoires", pageMemoires.getContent());
//      Variable pour indiquer que ce n'est pas une recherche
        model.addAttribute("rechercheEffectuee", false);
        // Gestion de l'utilisateur connecté
        if (principal != null) {
            Utilisateur utilisateur = memoireService.recherche_Utilisateur(principal.getName());
            if (utilisateur != null) {
                model.addAttribute("nom", utilisateur.getNom());
                model.addAttribute("prenom", utilisateur.getPrenom());

                String roles = utilisateur.getRoles().stream()
                        .map(Role::getRole)
                        .reduce((role1, role2) -> role1 + ", " + role2)
                        .orElse("Aucun rôle");
                model.addAttribute("roles", roles);
            }
            model.addAttribute("currentUser", principal.getName());
        }

        return "licence/licence";
    }


    @GetMapping("/master")
    public String afficherToutesLesMemoiresMasters(Model model, Principal principal,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Récupérer toutes les mémoires de type Master
        Page<Memoire> memoires = memoireService.getAllMemoiresMaster(pageable);

        // Ajouter les mémoires et les UFR au modèle
        model.addAttribute("pageMemoires", memoires); // pour la pagination
        model.addAttribute("memoires", memoires.getContent()); // pour le tableau
        model.addAttribute("rechercheEffectuee", false);

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

        return "master/master";
    }

   /**
     * Filtre et affiche uniquement les mémoires de Licence.
     */
   @PostMapping("/filtre/licence")
   public String filtrerMemoires(
           @RequestParam String ufrNom,
           @RequestParam String departementNom,
           @RequestParam String filiereNom,
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "2") int size,
           Model model, Principal principal) {

       Pageable pageable = PageRequest.of(page, size);
       Page<Memoire> pageMemoires = memoireService.getMemoiresLicenceFiltres(ufrNom, departementNom, filiereNom, pageable);
       // Regrouper les mémoires paginés
       Map<String, Map<String, List<Memoire>>> memoiresGroupes = pageMemoires.getContent().stream()
               .collect(Collectors.groupingBy(
                       m -> m.getFiliere().getDepartement().getUfr().getNom(),
                       Collectors.groupingBy(m -> m.getFiliere().getDepartement().getNom())
               ));

       model.addAttribute("ufrs", ufrService.findAllUfrs());
       model.addAttribute("memoiresGroupes", memoiresGroupes);
       model.addAttribute("pageMemoires", pageMemoires); // Pour pagination
       model.addAttribute("currentPage", page);
       model.addAttribute("totalPages", pageMemoires.getTotalPages());
       model.addAttribute("pageSize", size);

       model.addAttribute("selection", Map.of(
               "ufr", ufrNom,
               "departement", departementNom,
               "filiere", filiereNom
       ));
       model.addAttribute("rechercheEffectuee", true);
       // Gestion de l'utilisateur connecté
       if (principal != null) {
           Utilisateur utilisateur = memoireService.recherche_Utilisateur(principal.getName());
           if (utilisateur != null) {
               model.addAttribute("nom", utilisateur.getNom());
               model.addAttribute("prenom", utilisateur.getPrenom());

               String roles = utilisateur.getRoles().stream()
                       .map(Role::getRole)
                       .reduce((role1, role2) -> role1 + ", " + role2)
                       .orElse("Aucun rôle");
               model.addAttribute("roles", roles);
           }
           model.addAttribute("currentUser", principal.getName());
       }
       return "licence";
   }


    /**
     * Filtre et affiche uniquement les mémoires de Master.
     */
    @PostMapping("/filtre/master")
    public String filtrerMemoiresMaster(
            @RequestParam String ufrNom,
            @RequestParam String departementNom,
            @RequestParam String filiereNom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            Model model, Principal principal) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Memoire> pageMemoires =
                memoireService.getMemoiresMastersFiltres(
                        ufrNom, departementNom, filiereNom, pageable);

        Map<String, Map<String, List<Memoire>>> memoiresGroupes =
                pageMemoires.getContent().stream()
                        .collect(Collectors.groupingBy(
                                m -> m.getFiliere().getDepartement().getUfr().getNom(),
                                Collectors.groupingBy(
                                        m -> m.getFiliere().getDepartement().getNom()
                                )
                        ));

        model.addAttribute("ufrs", ufrService.findAllUfrs());
        model.addAttribute("memoiresGroupes", memoiresGroupes);
        model.addAttribute("pageMemoires", pageMemoires);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageMemoires.getTotalPages());
        model.addAttribute("pageSize", size);

        model.addAttribute("selection", Map.of(
                "ufr", ufrNom,
                "departement", departementNom,
                "filiere", filiereNom
        ));

        model.addAttribute("filtrageEffectue", true);
        model.addAttribute("rechercheEffectuee", false);

        return "master";
    }


    /**
     * Generation dattestation
     */
    @GetMapping("/memoires/genererAttestation/{id}")
    public String detailsMemoire(@PathVariable Long id, Model model) {
        Memoire memoire = memoireService.getMemoireById(id);
        String directeur = directeurService.getDirecteur().getNom();
        model.addAttribute("memoire", memoire);
        model.addAttribute("directeur", directeur);
        return "attestation/GenererAttestation"; // page de generation des attestations
    }



    @GetMapping("/memoires/genererAttestationLicence/{id}")
    public String MemoireLicence(@PathVariable Long id, Model model) {

        Memoire memoire = memoireService.getMemoireById(id);
        String directeur = directeurService.getDirecteur().getNom();

        model.addAttribute("memoire", memoire);
        model.addAttribute("directeur", directeur);

        return "attestation/GenererAttestionLicence";
    }

    //recherche par mots cles
    @GetMapping("/essaie/rechercheMotsCles")
    public String rechercherMemoire(@RequestParam(value = "motCle", required = false) String motCle,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                    Model model, Principal principal) {
        if (motCle != null && !motCle.trim().isEmpty()) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Memoire> pageResultats = memoireRepository.rechercherParTitreUfrDepartementFiliere(motCle.toLowerCase(), pageable);

            model.addAttribute("RechercheMemoiresParMotsCles", pageResultats.getContent());
            model.addAttribute("pageResultats", pageResultats);
            model.addAttribute("motCleRecherche", motCle);
        }
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

        // Si aucun critère n'est fourni, récupérer tous les mémoires groupés
        Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoireService.getMemoiresGroupes();
        model.addAttribute("memoiresGroupes", memoiresGroupes);
        model.addAttribute("notifications", notificationService.getNotificationNonLue());
        model.addAttribute("currentUser", principal.getName()); // Ajouter l'utilisateur actuel
        long nombreUtilisateurs = utilisateurService.getNombreUtilisateurs();
        model.addAttribute("nombreUtilisateurs", nombreUtilisateurs);
        statistiquesService.ajouterStatistiques(model);

        return "tableau_de_bord/dashboard_Admin";
    }

    @GetMapping("/dash/rechercheMotsCles")
    public String rechercherMemoireMotsCle(@RequestParam(value = "motCle", required = false) String motCle,
                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                           Model model, Principal principal) {
        if (motCle != null && !motCle.trim().isEmpty()) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Memoire> pageResultats = memoireRepository.rechercherParTitreUfrDepartementFiliere(motCle.toLowerCase(), pageable);

            model.addAttribute("RechercheMemoiresParMotsCles", pageResultats.getContent());
            model.addAttribute("pageResultats", pageResultats);
            model.addAttribute("motCleRecherche", motCle);
        }
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

        // Si aucun critère n'est fourni, récupérer tous les mémoires groupés
        Map<String, Map<String, List<Memoire>>> memoiresGroupes = memoireService.getMemoiresGroupes();
        model.addAttribute("memoiresGroupes", memoiresGroupes);
        model.addAttribute("notifications", notificationService.getNotificationNonLue());
        model.addAttribute("currentUser", principal.getName()); // Ajouter l'utilisateur actuel
        long nombreUtilisateurs = utilisateurService.getNombreUtilisateurs();
        model.addAttribute("nombreUtilisateurs", nombreUtilisateurs);
        statistiquesService.ajouterStatistiques(model);

        return "tableau_de_bord/dashboard_stager";
    }


    /**
     * Affiche la page de modification avec ou sans recherche.
     */

    @GetMapping("/modif")
    public String modif(Model model, Principal principal) {
         // Gestion de l'utilisateur connecté
        if (principal != null) {
            Utilisateur utilisateur = memoireService.recherche_Utilisateur(principal.getName());
            if (utilisateur != null) {
                model.addAttribute("nom", utilisateur.getNom());
                model.addAttribute("prenom", utilisateur.getPrenom());

                String roles = utilisateur.getRoles().stream()
                        .map(Role::getRole)
                        .reduce((role1, role2) -> role1 + ", " + role2)
                        .orElse("Aucun rôle");
                model.addAttribute("roles", roles);
            }
            model.addAttribute("memoire", new Memoire()); // Mémoire vide pour le formulaire
            model.addAttribute("etudiants", etudiantService.findAll());
            model.addAttribute("encadrants", encadrantService.findAll());
            model.addAttribute("filieres", filiereService.findAll());

            model.addAttribute("currentUser", principal.getName());
        }

        return "modif" +
                "";
    }

}


