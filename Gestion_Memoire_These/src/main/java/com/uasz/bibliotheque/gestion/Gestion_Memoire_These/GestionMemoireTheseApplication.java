package com.uasz.bibliotheque.gestion.Gestion_Memoire_These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Departement;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.EcoleDoctorat;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Filiere;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Ufr;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.DepartementRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.EcoleDoctoratRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.FiliereRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.UfrRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.MemoireService;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class GestionMemoireTheseApplication implements CommandLineRunner {

	@Autowired
	private UfrRepository ufrRepository;

	@Autowired
	private EcoleDoctoratRepository ecoleDoctoratRepository;

	@Autowired
	private DepartementRepository departementRepository;

	@Autowired
	private FiliereRepository filiereRepository;

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private UtilisateurService utilisateurService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(GestionMemoireTheseApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo() {
		return (args) -> {
			// Vérification et ajout des UFR si nécessaire
			insertUfr("UFR Sciences et Techniques (ST)");
			insertUfr("UFR Lettres, Art, Sciences Humaines (LASHU)");
			insertUfr("UFR Sciences Economiques et Sociales (SES)");
			insertUfr("UFR Sciences Santé (SS)");

			// Vérification et ajout des écoles doctorales
			insertEcoleDoctorat("Ecole Doctorat Sciences, Technologies et Ingénierie (ED-STI)", "UFR Sciences et Techniques (ST)");
			insertEcoleDoctorat("Ecole Doctorat Espaces, Sociétés et Humanités (ED-ESH)", "UFR Lettres, Art, Sciences Humaines (LASHU)");
			insertEcoleDoctorat("Ecole Doctorat Sante (ED-SS)", "UFR Sciences Santé (SS)");

			// Insertion des départements
			insertDepartement("Géographie", "UFR Sciences et Techniques (ST)");
			insertDepartement("Mathématiques", "UFR Sciences et Techniques (ST)");
			insertDepartement("Informatique", "UFR Sciences et Techniques (ST)");
			insertDepartement("Chimie", "UFR Sciences et Techniques (ST)");
			insertDepartement("Physique", "UFR Sciences et Techniques (ST)");
			insertDepartement("Agroforésterie", "UFR Sciences et Techniques (ST)");
			insertDepartement("Histoire", "UFR Lettres, Art, Sciences Humaines (LASHU)");
			insertDepartement("Lettres Modernes (LM)", "UFR Lettres, Art, Sciences Humaines (LASHU)");
			insertDepartement("Langues Etrangères Appliquées (LEA)", "UFR Lettres, Art, Sciences Humaines (LASHU)");
			insertDepartement("Management Informatique des Organisations (MIO)", "UFR Sciences Economiques et Sociales (SES)");
			insertDepartement("Sociologie", "UFR Sciences Economiques et Sociales (SES)");
			insertDepartement("Economie-Gestion (ECO-GES)", "UFR Sciences Economiques et Sociales (SES)");
			insertDepartement("Droit", "UFR Sciences Economiques et Sociales (SES)");
			insertDepartement("Tourisme", "UFR Sciences Economiques et Sociales (SES)");
			insertDepartement("Santé", "UFR Sciences Santé (SS)");

			// Insertion des filières
			insertFiliere("Aménagement du territoire", "Géographie");
			insertFiliere("Environnement", "Géographie");
			insertFiliere("Mathématiques appliquées", "Mathématiques");
			insertFiliere("Mathématiques Pures", "Mathématiques");
			insertFiliere("Génie logiciel", "Informatique");
			insertFiliere("Réseaux", "Informatique");
			insertFiliere("Intelligence Artificielle (IA)", "Informatique");
			insertFiliere("Chimie du Solide et des Materiaux (CSM)", "Chimie");
			insertFiliere("Synthèse Organique des Produits Naturels (SOPN)", "Chimie");
			insertFiliere("Physique des matériaux", "Physique");
			insertFiliere("Science des Atmosphères des Océans (SAO)", "Physique");
			insertFiliere("Energies Renouvelables", "Physique");
			insertFiliere("Agroforésterie", "Agroforésterie");
			insertFiliere("Archéologie", "Histoire");
			insertFiliere("Géopolitique", "Histoire");
			insertFiliere("Histoire de la Séné-Gambie", "Histoire");
			insertFiliere("Egyptologie", "Histoire");
			insertFiliere("Etudes Littéraires", "Lettres Modernes (LM)");
			insertFiliere("Sciences du Langage", "Lettres Modernes (LM)");
			insertFiliere("Développement Local", "Langues Etrangères Appliquées (LEA)");
			insertFiliere("Langues et Civilisations", "Langues Etrangères Appliquées (LEA)");
			insertFiliere("Méthodes Informatiques Appliquées à la Gestion (MIAGE)", "Management Informatique des Organisations (MIO)");
			insertFiliere("Management Informatique des Organisations (MIO)", "Management Informatique des Organisations (MIO)");
			insertFiliere("Management des Systèmes Informatique Appliqué (MSIA)", "Management Informatique des Organisations (MIO)");
			insertFiliere("Sociologie", "Sociologie");
			insertFiliere("Finance et Développement", "Economie-Gestion (ECO-GES)");
			insertFiliere("Evaluation d'impact", "Economie-Gestion (ECO-GES)");
			insertFiliere("Economie", "Economie-Gestion (ECO-GES)");
			insertFiliere("Gestion", "Economie-Gestion (ECO-GES)");
			insertFiliere("Droit des Collectivités Locales", "Droit");
			insertFiliere("Droit Humanitaire", "Droit");
			insertFiliere("Droit des affaires", "Droit");
			insertFiliere("Tourisme", "Tourisme");
			insertFiliere("Médecine", "Santé");
			insertFiliere("Pharmacie", "Santé");
			insertFiliere("Sciences infirmières", "Santé");
			insertFiliere("Biologie", "Santé");
			insertFiliere("Obstétrique", "Santé");

		};
	}

	private void insertUfr(String nom) {
		if (!ufrRepository.existsByNom(nom)) {
			Ufr ufr = new Ufr(nom);
			ufrRepository.save(ufr);
		}
	}

	private void insertEcoleDoctorat(String nom, String ufrNom) {
		Ufr ufr = ufrRepository.findByNom(ufrNom).orElse(null);
		if (ufr != null && !ecoleDoctoratRepository.existsByNom(nom)) {
			EcoleDoctorat ecoleDoctorat = new EcoleDoctorat(nom, ufr);
			ecoleDoctoratRepository.save(ecoleDoctorat);
		}
	}

	private void insertDepartement(String nom, String ufrNom) {
		Ufr ufr = ufrRepository.findByNom(ufrNom).orElse(null);
		if (ufr != null && !departementRepository.existsByNomAndUfr(nom, ufr)) {
			Departement departement = new Departement(nom, ufr);
			departementRepository.save(departement);
		}
	}

	private void insertFiliere(String nom, String departementNom) {
		Departement departement = departementRepository.findByNom(departementNom).orElse(null);
		if (departement != null && !filiereRepository.existsByNomAndDepartement(nom, departement)) {
			Filiere filiere = new Filiere(nom, departement);
			filiereRepository.save(filiere);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		// Vérification si l'utilisateur existe avant d'ajouter
		insertUtilisateur("idiop@uasz.sn", "Ibrahima", "DIOP", "Passer123", "Responsable");
		insertUtilisateur("cmalack@uasz.sn", "Camin", "MALACK", "Passer123", "Stager");

		// Insérer les départements et les filières ici...
	}

	private void insertUtilisateur(String username, String prenom, String nom, String password, String role) {
		if (!utilisateurRepository.existsByUsername(username)) {
			String encodedPassword = passwordEncoder.encode(password);
			Utilisateur utilisateur = new Utilisateur();
			utilisateur.setUsername(username);
			utilisateur.setPrenom(prenom);
			utilisateur.setNom(nom);
			utilisateur.setPassword(encodedPassword);
			utilisateur.setDateCreation(new Date());
			utilisateurService.ajouter_Utilisateur(utilisateur);

			Role utilisateurRole = utilisateurService.ajouter_role(new Role(role));
			utilisateurService.ajouter_UtilisateurRoles(utilisateur, utilisateurRole);
		}
	}
}
