package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.TypeMemoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.MemoireService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.TheseService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.TheseSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/licences/recherche", method = RequestMethod.POST)
    public String afficherMemoiresLicence(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) Integer annee,
            Model model
    ) {
        // Définir une spécification de base pour les mémoires de type Licence
        Specification<Memoire> spec = Specification.where(MemoireSpecifications.withType(TypeMemoire.LICENCE));

        // Vérifier si des critères de recherche ont été fournis
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

        // Si la recherche est effectuée, récupérer les résultats
        if (hasSearchParams) {
            List<Memoire> memoiresTrouves = memoireService.searchMemos(spec);
            model.addAttribute("memoires", memoiresTrouves);
            model.addAttribute("nombreMemoiresTrouves", memoiresTrouves.size());

            if (memoiresTrouves.isEmpty()) {
                model.addAttribute("message", "Aucun mémoire trouvé pour les critères spécifiés.");
            }

            model.addAttribute("rechercheEffectuee", true);
        } else {
            model.addAttribute("rechercheEffectuee", false);
        }

        model.addAttribute("typeMemoire", "Licence");
        return "licence"; // Vue dédiée
    }

    // recherche de mémoires de Master
    @RequestMapping(value = "/masters/recherche", method = RequestMethod.POST)
    public String afficherMemoiresMaster(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) String filiere,
            @RequestParam(required = false) Integer annee,
            Model model
    ) {
        // Définir une spécification de base pour les mémoires de type Licence
        Specification<Memoire> spec = Specification.where(MemoireSpecifications.withType(TypeMemoire.MASTER));

        // Vérifier si des critères de recherche ont été fournis
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

        // Si la recherche est effectuée, récupérer les résultats
        if (hasSearchParams) {
            List<Memoire> memoiresTrouves = memoireService.searchMemos(spec);
            model.addAttribute("memoires", memoiresTrouves);
            model.addAttribute("nombreMemoiresTrouves", memoiresTrouves.size());

            if (memoiresTrouves.isEmpty()) {
                model.addAttribute("message", "Aucun mémoire trouvé pour les critères spécifiés.");
            }

            model.addAttribute("rechercheEffectuee", true);
        } else {
            model.addAttribute("rechercheEffectuee", false);
        }

        model.addAttribute("typeMemoire", "Master");
        return "master"; // Vue dédiée
    }

    @RequestMapping(value = "/theses/recherche", method = RequestMethod.POST)
    public String afficherTheses(
            @RequestParam(required = false) String cote,
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) String encadrant,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) String ecoleDoctoraleNom, // Ajouté pour le filtrage
            @RequestParam(required = false) String ufrNom, // Ajouté pour le filtrage
            Model model
    ) {
        // Définir une spécification de base pour les thèses
        Specification<These> spec = Specification.where(null); // Initialise une spécification vide

        // Vérifier si des critères de recherche ont été fournis
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

        // Si la recherche est effectuée, récupérer les résultats
        if (hasSearchParams) {
            List<These> thesesTrouvees = theseService.searchMemos(spec);
            model.addAttribute("theses", thesesTrouvees);
            model.addAttribute("nombreThesesTrouvees", thesesTrouvees.size());

            if (thesesTrouvees.isEmpty()) {
                model.addAttribute("message", "Aucune thèse trouvée pour les critères spécifiés.");
            }

            model.addAttribute("rechercheEffectuee", true);
        } else {
            model.addAttribute("rechercheEffectuee", false);
        }

        // Ajouter d'autres attributs nécessaires
        model.addAttribute("typeThese", "Doctorat");

        return "doctorat"; // Vue dédiée
    }


}
