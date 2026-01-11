package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.TypeMemoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.MemoireService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.TheseService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.TheseSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller // Utilisation correcte de l'annotation pour Thymeleaf
public class RechercheController {

    private final MemoireService memoireService;

    private final TheseService theseService;

    @Autowired
    public RechercheController(MemoireService memoireService, TheseService theseService){
        this.memoireService = memoireService;
        this.theseService = theseService;
    }

    // recherche de mémoires de Licence
    @RequestMapping(value = "/licences/recherche", method = {RequestMethod.POST, RequestMethod.GET})
    public String afficherMemoiresLicence(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) Integer annee,
            @RequestParam(defaultValue = "0") int page, // pagination
            @RequestParam(defaultValue = "10") int size, // taille de page par défaut
            Model model, Principal principal
    ) {
        Specification<Memoire> spec = Specification.where(MemoireSpecifications.withType(TypeMemoire.LICENCE));
        boolean hasSearchParams = false;

        if (cote != null && !cote.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withCote(cote));
            hasSearchParams = true;
        }
        if (titre != null && !titre.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withTitre(titre));
            hasSearchParams = true;
        }
        if (etudiant != null && !etudiant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEtudiant(etudiant));
            hasSearchParams = true;
        }
        if (encadrant != null && !encadrant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEncadrant(encadrant));
            hasSearchParams = true;
        }
        if (filiere != null && !filiere.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withFiliere(filiere));
            hasSearchParams = true;
        }
        if (annee != null) {
            spec = spec.and(MemoireSpecifications.withAnnee(annee));
            hasSearchParams = true;
        }

        if (hasSearchParams) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Memoire> pageMemoires = memoireService.searchMemos(spec, pageable);

            model.addAttribute("pageMemoires", pageMemoires);
            model.addAttribute("memoires", pageMemoires.getContent());
            model.addAttribute("nombreMemoiresTrouves", pageMemoires.getTotalElements());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageMemoires.getTotalPages());
            model.addAttribute("pageSize", size); // <= AJOUTE CETTE LIGNE

            if (pageMemoires.isEmpty()) {
                model.addAttribute("message", "Aucun mémoire trouvé pour les critères spécifiés.");
            }

            model.addAttribute("rechercheEffectuee", true);
        } else {
            model.addAttribute("rechercheEffectuee", false);
        }

        model.addAttribute("typeMemoire", "Licence");
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


    // recherche de mémoires de Master
    @RequestMapping(value = "/masters/recherche", method = {RequestMethod.POST, RequestMethod.GET})
    public String afficherMemoiresMaster(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) Integer annee,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model, Principal principal
    ) {
        Specification<Memoire> spec = Specification.where(MemoireSpecifications.withType(TypeMemoire.MASTER));
        boolean hasSearchParams = false;

        if (cote != null && !cote.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withCote(cote));
            hasSearchParams = true;
        }
        if (titre != null && !titre.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withTitre(titre));
            hasSearchParams = true;
        }
        if (etudiant != null && !etudiant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEtudiant(etudiant));
            hasSearchParams = true;
        }
        if (encadrant != null && !encadrant.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEncadrant(encadrant));
            hasSearchParams = true;
        }
        if (filiere != null && !filiere.isEmpty()) {
            spec = spec.and(MemoireSpecifications.withFiliere(filiere));
            hasSearchParams = true;
        }
        if (annee != null) {
            spec = spec.and(MemoireSpecifications.withAnnee(annee));
            hasSearchParams = true;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Memoire> pageMemoires = memoireService.searchMemos(spec, pageable);

        model.addAttribute("pageMemoires", pageMemoires);
        model.addAttribute("memoires", pageMemoires.getContent());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageMemoires.getTotalPages());
        model.addAttribute("totalElements", pageMemoires.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("nombreMemoiresTrouves", pageMemoires.getTotalElements());
        model.addAttribute("rechercheEffectuee", hasSearchParams); // au lieu de "recherche"
        model.addAttribute("typeMemoire", "Master");
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
        return "master";
    }


    @RequestMapping(value = "/theses/recherche", method = {RequestMethod.POST, RequestMethod.GET})
    public String rechercherTheses(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) String ecoleDoctoraleNom,
            @RequestParam(required = false) String ufrNom,
            Model model, Principal principal
    ) {
        Specification<These> spec = Specification.where(null);
        boolean hasSearchParams = false;

        if (cote != null && !cote.isEmpty()) {
            spec = spec.and(TheseSpecifications.withCote(cote));
            hasSearchParams = true;
        }
        if (titre != null && !titre.isEmpty()) {
            spec = spec.and(TheseSpecifications.withTitre(titre));
            hasSearchParams = true;
        }
        if (etudiant != null && !etudiant.isEmpty()) {
            spec = spec.and(TheseSpecifications.withEtudiant(etudiant));
            hasSearchParams = true;
        }
        if (encadrant != null && !encadrant.isEmpty()) {
            spec = spec.and(TheseSpecifications.withEncadrant(encadrant));
            hasSearchParams = true;
        }
        if (annee != null) {
            spec = spec.and(TheseSpecifications.withAnnee(annee));
            hasSearchParams = true;
        }
        if (ecoleDoctoraleNom != null && !ecoleDoctoraleNom.isEmpty()) {
            spec = spec.and(TheseSpecifications.withEcoleDoctorat(ecoleDoctoraleNom));
            hasSearchParams = true;
        }
        if (ufrNom != null && !ufrNom.isEmpty()) {
            spec = spec.and(TheseSpecifications.withUFR(ufrNom));
            hasSearchParams = true;
        }

        // ✅ Initialisation toujours
        List<These> thesesListe = new ArrayList<>();
        long nombreThesesTrouvees = 0;
        boolean rechercheEffectuee = false;

        if (hasSearchParams) {
            thesesListe = theseService.searchMemos(spec);
            nombreThesesTrouvees = thesesListe.size();
            rechercheEffectuee = true;
        }

        // Toujours ajouter ces attributs pour Thymeleaf
        model.addAttribute("thesesListe", thesesListe);
        model.addAttribute("nombreThesesTrouvees", nombreThesesTrouvees);
        model.addAttribute("rechercheEffectuee", rechercheEffectuee);
        model.addAttribute("typeThese", "Doctorat");

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

        return "doctorat";
    }



}
