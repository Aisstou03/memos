package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class StagerController {

    @Autowired
    private MemoireService memoireService;
    @Autowired
    EtudiantService etudiantService;

    @Autowired
    EncadrantService encadrantService;
    @Autowired
    FiliereService filiereService;
    @Autowired
    private UfrService ufrService;

    @Autowired
    private DepartementService departementService;

    private final StatistiquesService statistiquesService;
    @Autowired
    private NotificationService notificationService ;

    @Autowired
    public StagerController(MemoireService memoireService, StatistiquesService statistiquesService) {
        this.memoireService = memoireService;
        this.statistiquesService = statistiquesService;
    }
    @GetMapping("/dashbord/stager")
    public String affdashbord(Model model, Principal principal){
        // Gestion de l'utilisateur connecté
        if (principal != null) {
            Utilisateur utilisateur = memoireService.recherche_Utilisateur(principal.getName());
            if (utilisateur != null) {
                // Ajouter les informations de l'utilisateur au modèle
                model.addAttribute("nom", utilisateur.getNom());
                model.addAttribute("prenom", utilisateur.getPrenom());
                model.addAttribute("notifications", notificationService.getNotificationNonLue());
                model.addAttribute("currentUser", principal.getName()); // Ajouter l'utilisateur actuel

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

        statistiquesService.ajouterStatistiques(model);

        return "dashboard" ;
    }
}
