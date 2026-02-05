package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.controller;


import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.EmailService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.UtilisateurService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;

@Controller
public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService ;

    /**
     * Affiche la page de connexion.
     */
    @RequestMapping(value = "/login")
    public String index() {
        return "login";
    }

    /**
     * Redirige l'utilisateur connecté vers le tableau de bord approprié en fonction de son rôle.
     */
    @RequestMapping("/")
    public String login(Principal principal) {
        if (principal != null) {
            Utilisateur utilisateur = utilisateurService.recherche_Utilisateur(principal.getName());
            if (utilisateur != null && utilisateur.getRoles() != null && !utilisateur.getRoles().isEmpty()) {
                String role = utilisateur.getRoles().get(0).getRole();
                return switch (role) {
                    case "Responsable", "Admin" -> "redirect:/memoires/liste";
                    case "Stager" -> "redirect:/dash/rechercheMotsCles";
                    default -> "redirect:/login?error=role_inconnu";
                };
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "widget/acces-denied";  // Fichier Thymeleaf: access-denied.html
    }

    /**
     * Affiche la page de succès après une inscription réussie.
     */
    @GetMapping("/success")
    public String afficherPageSucces() {
        return "widget/success";
    }
    /**
     * Gère l'inscription d'un nouvel utilisateur.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ajouterUtilisateur")
    public String ajouterUtilisateur(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String role) {

        // Vérifier si l'utilisateur existe déjà
        if (utilisateurService.recherche_Utilisateur(username) != null) {
            return "redirect:/error"; // Redirection en cas d'email déjà existant
        }
        // Créer et configurer un nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(username);
        utilisateur.setPassword(passwordEncoder.encode(password)); // Encode le mot de passe
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        // Récupérer ou créer le rôle
        Role roleEntity = utilisateurService.recherche_role(role);
        if (roleEntity == null) {
            roleEntity = utilisateurService.ajouter_role(new Role(role));
        }
        // Associer le rôle à l'utilisateur
        utilisateur.getRoles().add(roleEntity);

        // Sauvegarder l'utilisateur
        utilisateurService.ajouter_Utilisateur(utilisateur);

        // Envoyer un e-mail avec les informations de connexion
        emailService.sendAccountCreationEmail(username, username, password);

        return "redirect:/listeResponsables"; // Redirection après succès
    }
    /**
     * Gère la déconnexion de l'utilisateur et redirige vers la page de connexion.
     */
    @RequestMapping(value = "/logout")
    public String logOutAndRedirectToLoginPage(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout=true";
    }
    /**
     * Affiche la liste des utilisateurs.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listeResponsables")
    public String listeResponsables(Model model, Principal principal) {
        // Récupère la liste des responsables depuis le service
        List<Utilisateur> responsables = utilisateurService.listUtilisateur();
        model.addAttribute("listeResponsables", responsables); // Transfert des données au modèle
        model.addAttribute("notifications", notificationService.getNotificationNonLue());
        model.addAttribute("currentUser", principal.getName()); // Ajouter l'utilisateur actuel

        return "Utilisateur/AjoutUtilisateur"; // Assurez-vous que ce nom correspond au fichier Thymeleaf (ex: responsables.html)
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ajouterUtilisateur")
        public String AjouterUser(){
        return "Utilisateur/ajoutUser";
    }

    @PostMapping("/ajouterResponsable")
    public String ajouterResponsable(Utilisateur user){
        utilisateurService.ajouter_Utilisateur(user);
        return "redirect:/listeResponsables";
    }

    @PostMapping("/modifierResponsable")
    public String modifierResponsable(@ModelAttribute Utilisateur utilisateur) {
        Utilisateur User = utilisateurRepository.findById(utilisateur.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        // Mettre à jour uniquement les champs modifiables
        User.setNom(utilisateur.getNom());
        User.setPrenom(utilisateur.getPrenom());
        User.setUsername(utilisateur.getUsername());
        utilisateurService.modifierUtilisateur(User);
        return "redirect:/listeResponsables";
    }

    @PostMapping("/supprimerResponsable")
    public String supprimerResponsable(Utilisateur user) {
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            emailService.sendAccountDeletionEmail(user.getUsername());
        } else {
            System.err.println("L'adresse e-mail est nulle, impossible d'envoyer le mail.");
        }
        utilisateurService.supprimerResponsable(user);
        return "redirect:/listeResponsables";
    }

    @GetMapping("/profil")
    public String afficherProfil(Model model, Principal principal) {
        if (principal != null) {
            Utilisateur utilisateur = utilisateurService.recherche_Utilisateur(principal.getName());
            model.addAttribute("utilisateur", utilisateur);
        }
        return "Utilisateur/profil"; // Assurez-vous de créer un fichier `profil.html`
    }

    @PostMapping("/modifierMotDePasse")
    public String modifierMotDePasse(
            @RequestParam String ancienPassword,
            @RequestParam String nouveauPassword,
            @RequestParam String confirmationPassword, // Ajout de la confirmation
            Principal principal,
            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        // Vérifier que le nouveau mot de passe et sa confirmation correspondent
        if (!nouveauPassword.equals(confirmationPassword)) {
            redirectAttributes.addFlashAttribute("erreurMotDePasse", "Le nouveau mot de passe et sa confirmation ne correspondent pas !");
            return "redirect:/profil";
        }
        Utilisateur utilisateur = utilisateurService.recherche_Utilisateur(principal.getName());
        // Vérifier si l'ancien mot de passe est correct
        if (!passwordEncoder.matches(ancienPassword, utilisateur.getPassword())) {
            redirectAttributes.addFlashAttribute("erreurMotDePasse", "Ancien mot de passe incorrect !");
            return "redirect:/profil";
        }
        // Mettre à jour le mot de passe avec le nouveau haché
        utilisateur.setPassword(passwordEncoder.encode(nouveauPassword));
        utilisateurService.modifierUtilisateur(utilisateur);
        redirectAttributes.addFlashAttribute("messageMotDePasse", "Mot de passe modifié avec succès !");
        return "redirect:/profil";
    }

    @PostMapping("/modifierProfil")
    public String modifierProfil(
            @RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String username,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        Utilisateur utilisateur = utilisateurService.recherche_Utilisateur(principal.getName());
        // Vérifier si l'email est déjà utilisé par un autre utilisateur
        Utilisateur utilisateurExistant = utilisateurService.recherche_Utilisateur(username);
        if (utilisateurExistant != null && !utilisateurExistant.getUsername().equals(utilisateur.getUsername())) {
            redirectAttributes.addFlashAttribute("erreur", "Cet email est déjà utilisé !");
            return "redirect:/profil";
        }
        // Mise à jour des informations
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setUsername(username);
        utilisateurService.modifierUtilisateur(utilisateur);
        redirectAttributes.addFlashAttribute("message", "Informations mises à jour avec succès !");
        return "redirect:/listeResponsables";
    }
}
