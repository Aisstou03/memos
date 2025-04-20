package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Notification.service.NotificationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.*;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemoireService {
    @Autowired
    private MemoireRepository memoireRepository;
    @Autowired
    private MotCleRepository motCleRepository;

    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private UfrRepository ufrRepository;
    @Autowired
    public UserRepository userRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EncadrantRepository encadrantRepository;

    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private EncadrantService encadrantService;

    @Autowired
    private FiliereService filiereService;

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private TheseRepository theseRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    // Récupère toutes les filières
    public List<Filiere> getAllFilieres() {
        return filiereRepository.findAll();
    }

    // Méthode pour récupérer tous les mémoires
    public List<Memoire> getAllMemoires() {
        return memoireRepository.findAll();
    }

    // Méthode pour récupérer un mémoire par son ID
    public Memoire getMemoireById(Long id) {
        Optional<Memoire> memoire = memoireRepository.findById(id);
        return memoire.orElseThrow(() -> new RuntimeException("Mémoire non trouvé"));
    }

    // Méthode pour générer la cote basée sur la filière, l'année et le nombre d'exemplaires
    public String genererCote(TypeMemoire type, Filiere filiere, int annee, int exemplaires) {
        String prefixeType = "";
        switch (type) {
            case LICENCE -> prefixeType = "L";
            case MASTER -> prefixeType = "M";
            case DOCTORAT -> prefixeType = "D";
        }

        String initialesFiliere = convertirEnInitiales(filiere.getNom());
        return prefixeType + initialesFiliere + (annee % 100) + "/" + exemplaires;
    }

    // Méthode pour convertir la filière en initiales
    private String convertirEnInitiales(String filiere) {
        StringBuilder initiales = new StringBuilder();
        String[] mots = filiere.split("_");
        for (String mot : mots) {
            if (!mot.isEmpty()) {
                initiales.append(mot.charAt(0));
            }
        }
        return initiales.toString().toUpperCase();
    }

    // Ajouter une mémoire
    public void ajouterMemoire(String ufrNom, String departementNom, String filiereNom, String type, String titre, int annee,
                               int exemplaires, String etudiantNom, String encadrantNom, List<String> motsCles, boolean licencePro) {
        // Obtenir la filière
        Filiere filiere = filiereRepository.findByNom(filiereNom)
                .orElseThrow(() -> new RuntimeException("Filière introuvable"));

        // Récupérer le département et l'UFR
        Departement departement = departementRepository.findByNom(departementNom)
                .orElseThrow(() -> new RuntimeException("Département introuvable"));

        Ufr ufr = ufrRepository.findByNom(ufrNom)
                .orElseThrow(() -> new RuntimeException("UFR introuvable"));

        // Vérifier que le département correspond bien à l'UFR
        if (!departement.getUfr().equals(ufr)) {
            throw new RuntimeException("L'UFR ne correspond pas à celui du département pour cette filière");
        }

        // Gérer l'étudiant
        Etudiant etudiant = etudiantRepository.findByNom(etudiantNom)
                .orElseGet(() -> etudiantRepository.save(new Etudiant(null, etudiantNom)));

        // Gérer l'encadrant
        Encadrant encadrant = encadrantRepository.findByNomAndFiliere(encadrantNom, filiere)
                .orElseGet(() -> encadrantRepository.save(new Encadrant(null, encadrantNom, filiere)));

        // Créer l'objet Mémoire
        Memoire memoire = new Memoire();
        memoire.setTitre(titre);
        memoire.setAnnee(annee);
        memoire.setExemplaires(exemplaires);
        memoire.setEtudiant(etudiant);
        memoire.setEncadrant(encadrant);
        memoire.setType(TypeMemoire.valueOf(type.toUpperCase()));
        memoire.setFiliere(filiere);
        memoire.setDepartement(departement);
        memoire.setUfr(ufr);
        // Gérer les mots-clés
        List<MotCle> motsClesEntities = new ArrayList<>();
        for (String mot : motsCles) {
            String motNettoye = mot.trim().toLowerCase();
            MotCle motCle = motCleRepository.findByValeur(motNettoye)
                    .orElseGet(() -> motCleRepository.save(new MotCle(motNettoye)));
            motsClesEntities.add(motCle);
        }
        memoire.setMotsCles(motsClesEntities);
        memoire.setLicencePro(licencePro);

        // Générer la cote
        String cote = genererCote(memoire.getType(), filiere, annee, exemplaires);
        memoire.setCote(cote);

        // Sauvegarder le mémoire
        memoireRepository.save(memoire);

        // Créer une notification
        String messageNotification = "Un nouveau mémoire a été ajouté : " + titre + " de : " + etudiantNom;
        notificationService.creerNotification(messageNotification);

        // Log console
        System.out.println("Notification créée : " + messageNotification);
    }

    // Méthode pour modifier un mémoire existant
    public Memoire modifierMemoire(Long id, Memoire memoireModifiee) {
        // Vérifie si le mémoire existe
        Memoire memoireExistante = getMemoireById(id);
        if (memoireExistante == null) {
            throw new IllegalArgumentException("Mémoire avec ID " + id + " n'existe pas.");
        }

        // Gestion de l'étudiant
        Etudiant etudiant = null;
        if (memoireModifiee.getEtudiant() != null) {
            if (memoireModifiee.getEtudiant().getId() != null) {
                // Si l'ID de l'étudiant est fourni, récupérer l'étudiant existant
                etudiant = etudiantService.findById(memoireModifiee.getEtudiant().getId());
            } else {
                // Sinon, créer un nouvel étudiant avec les informations fournies
                etudiant = etudiantService.createNewStudent(memoireModifiee.getEtudiant());
            }
        }

        // Gestion de l'encadrant
        Encadrant encadrant = null;
        if (memoireModifiee.getEncadrant() != null) {
            if (memoireModifiee.getEncadrant().getId() != null) {
                // Si l'ID de l'encadrant est fourni, récupérer l'encadrant existant
                encadrant = encadrantService.findById(memoireModifiee.getEncadrant().getId());
            } else {
                // Sinon, créer un nouvel encadrant avec les informations fournies
                encadrant = encadrantService.createNewSupervisor(memoireModifiee.getEncadrant());
            }
        }

        // Gestion de la filière
        Filiere filiere = null;
        if (memoireModifiee.getFiliere() != null && memoireModifiee.getFiliere().getNom() != null) {
            filiere = filiereService.findByNom(memoireModifiee.getFiliere().getNom())
                    .orElseGet(() -> filiereService.createNewField(memoireModifiee.getFiliere()));
        }

        // Mise à jour des champs du mémoire existant
        memoireExistante.setTitre(memoireModifiee.getTitre());
        memoireExistante.setEtudiant(etudiant);
        memoireExistante.setEncadrant(encadrant);
        memoireExistante.setFiliere(filiere);
        memoireExistante.setAnnee(memoireModifiee.getAnnee());
        memoireExistante.setExemplaires(memoireModifiee.getExemplaires());

        // Regénérer la cote avec les nouvelles informations
        TypeMemoire type = memoireExistante.getType(); // Assurez-vous que TypeMemoire est bien défini
        String nouvelleCote = genererCote(type, filiere, memoireModifiee.getAnnee(), memoireModifiee.getExemplaires());
        memoireExistante.setCote(nouvelleCote);  // Mise à jour de la cote

        // Sauvegarde et retour de l'entité mise à jour
        Memoire memoireSauvegardee = memoireRepository.save(memoireExistante);

        // Création de la notification après mise à jour
        String titre = memoireSauvegardee.getTitre();
        String etudiantNom = memoireSauvegardee.getEtudiant() != null ? memoireSauvegardee.getEtudiant().getNom() : "Inconnu";
        String messageNotification = "Le mémoire \"" + titre + "\" de " + etudiantNom + " a été modifié.";
        notificationService.creerNotification(messageNotification);

        // Journalisation pour vérifier le fonctionnement
        System.out.println("Notification créée : " + messageNotification);

        return memoireSauvegardee;
    }


    public List<Memoire> rechercherMemos(Map<String, String> params) {
        Specification<Memoire> spec = Specification.where(null);

        // Construire la spécification dynamiquement en fonction des paramètres
        if (params.containsKey("cote") && !params.get("cote").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withCote(params.get("cote")));
        }
        if (params.containsKey("titre") && !params.get("titre").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withTitre(params.get("titre")));
        }
        if (params.containsKey("etudiant") && !params.get("etudiant").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEtudiant(params.get("etudiant")));
        }
        if (params.containsKey("encadrant") && !params.get("encadrant").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withEncadrant(params.get("encadrant")));
        }
        if (params.containsKey("annee") && !params.get("annee").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withAnnee(Integer.parseInt(params.get("annee"))));
        }
        if (params.containsKey("filiere") && !params.get("filiere").isEmpty()) {
            spec = spec.and(MemoireSpecifications.withFiliere(params.get("filiere")));
        }
        // Ajouter un filtre pour le type de mémoire (Licence, Master, Doctorat)
        if (params.containsKey("type") && !params.get("type").isEmpty()) {
            String typeParam = params.get("type");
            try {
                TypeMemoire typeMemoire = TypeMemoire.valueOf(typeParam.toUpperCase());  // Assurez-vous que la valeur est en majuscule pour correspondre à l'enum
                spec = spec.and(MemoireSpecifications.withType(typeMemoire));
            } catch (IllegalArgumentException e) {
                // Si le type n'est pas valide, loggez l'erreur
                System.out.println("Type invalide : " + typeParam);
            }
        }

        return memoireRepository.findAll(spec);
    }



    public Map<String, Map<String, List<Memoire>>> getMemoiresGroupes() {
        List<Memoire> memoires = getAllMemoires();
        Map<String, Map<String, List<Memoire>>> groupedMemoires = new HashMap<>();

        for (Memoire memoire : memoires) {
            String ufrNom = memoire.getUfr() != null ? memoire.getUfr().getNom() : "Inconnu";
            String departementNom = memoire.getDepartement() != null ? memoire.getDepartement().getNom() : "Inconnu";

            // Grouper par UFR puis par Département
            groupedMemoires
                    .computeIfAbsent(ufrNom, k -> new HashMap<>())
                    .computeIfAbsent(departementNom, k -> new ArrayList<>())
                    .add(memoire);
        }
        return groupedMemoires;
    }

    public Page<Memoire> searchMemos(Specification<Memoire> spec, Pageable pageable) {
        return memoireRepository.findAll(spec, pageable);
    }


    public long countMemosByTypeNonSupprime(TypeMemoire type) {
        return memoireRepository.countNonSupprimeMemosByType(type);
    }

    public long countThesesNonSupprime() {
        return theseRepository.countNonSupprimeTheses();
    }

    public Map<Integer, Long> countMemosByTypeAndYear(TypeMemoire type) {
        // Récupérer uniquement les mémoires non supprimés
        List<Memoire> memoires = memoireRepository.findAllByTypeAndNotDeleted(type);
        return memoires.stream()
                .collect(Collectors.groupingBy(
                        Memoire::getAnnee,   // Regrouper par année
                        Collectors.counting() // Compter les mémoires dans chaque année
                ));
    }

    public Map<Integer, Long> countThesesByYear() {
        // Récupérer uniquement les thèses non supprimées
        List<These> theses = theseRepository.findAllNotDeleted();
        return theses.stream()
                .collect(Collectors.groupingBy(
                        These::getAnnee,    // Regrouper par année
                        Collectors.counting() // Compter les thèses dans chaque année
                ));
    }

    //liste de licences filtres
    /**
     * Récupère uniquement les mémoires de Licence selon les filtres (UFR, Département, Filière).
     */
    public Page<Memoire> getMemoiresLicenceFiltres(String ufrNom, String departementNom, String filiereNom, Pageable pageable) {
        Specification<Memoire> spec = Specification
                .where(MemoireSpecifications.withType(TypeMemoire.LICENCE))
                .and(MemoireSpecifications.withUFR(ufrNom))
                .and(MemoireSpecifications.withDepartement(departementNom))
                .and(MemoireSpecifications.withFiliere(filiereNom));

        return memoireRepository.findAll(spec, pageable);
    }

    //liste de memoires filtres
    /**
     * Récupère uniquement les mémoires de masters selon les filtres (UFR, Département, Filière).
     */
    public Page<Memoire> getMemoiresMastersFiltres(String ufrNom, String departementNom, String filiereNom, Pageable pageable) {
        Specification<Memoire> spec = Specification
                .where(MemoireSpecifications.withType(TypeMemoire.MASTER))
                .and(MemoireSpecifications.withUFR(ufrNom))
                .and(MemoireSpecifications.withDepartement(departementNom))
                .and(MemoireSpecifications.withFiliere(filiereNom));

        return memoireRepository.findAll(spec, pageable);
    }

    // Récupérer les mémoires actifs (hors corbeille) de type LICENCE
    // Mémoires actifs (hors corbeille) de type LICENCE
    public Page<Memoire> findMemoiresActifs(Pageable pageable) {
        return memoireRepository.findByCorbeilleFalseAndType(TypeMemoire.LICENCE, pageable);
    }

    // Mémoires actifs de type MASTER
    public Page<Memoire> getAllMemoiresMaster(Pageable pageable) {
        return memoireRepository.findByCorbeilleFalseAndType(TypeMemoire.MASTER, pageable);
    }


    // Mémoires supprimés (corbeille) LICENCE
    public Page<Memoire> getMemoiresSupprimesLicence(Pageable pageable) {
        return memoireRepository.findByCorbeilleTrueAndType(TypeMemoire.LICENCE, pageable);
    }

    // Mémoires supprimés (corbeille) MASTER
    public Page<Memoire> getMemoiresSupprimesMaster(Pageable pageable) {
        return memoireRepository.findByCorbeilleTrueAndType(TypeMemoire.MASTER, pageable);
    }

    //liste de these
    public Map<String, Map<String, List<Memoire>>> getMemoiresDoctoratGroupes() {
        // Récupérer tous les mémoires de type Doctorat
        List<Memoire> memoiresDoctorat = memoireRepository.findByType(TypeMemoire.DOCTORAT);

        if (memoiresDoctorat == null || memoiresDoctorat.isEmpty()) {
            return new HashMap<>(); // Retourne un map vide s'il n'y a pas de mémoire
        }

        // Grouper les mémoires par UFR > Département
        return memoiresDoctorat.stream()
                .collect(Collectors.groupingBy(
                        memoire -> memoire.getFiliere().getDepartement().getUfr().getNom(),
                        Collectors.groupingBy(
                                memoire -> memoire.getFiliere().getDepartement().getNom()
                        )
                ));
    }

    //Mon code pour recuperer les information de l'utilisateur qui est connecter
    public Utilisateur recherche_Utilisateur(String username){
        Utilisateur user = userRepository.findUtilisateurByUsername(username);
        return user;
    }
    public List<Memoire> findByAnneeAndType(int annee, String type) {
        try {
            // Conversion si nécessaire
            TypeMemoire typeEnum = TypeMemoire.valueOf(type.toUpperCase());
            return memoireRepository.findByAnneeAndType(annee, typeEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Type de mémoire invalide : " + type, e);
        }
    }

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional// Si la méthode ne modifie pas la base de données
    public List<Memoire> rechercherMemoire(Integer annee, TypeMemoire type, String ufr, String departement, String filiere) {
        // Construire un exemple de critère de recherche
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Memoire> query = cb.createQuery(Memoire.class);
        Root<Memoire> memoire = query.from(Memoire.class);

        List<Predicate> predicates = new ArrayList<>();

        // Ajouter les conditions selon les paramètres fournis
        if (annee != null) {
            predicates.add(cb.equal(memoire.get("annee"), annee));
        }
        if (type != null) {
            predicates.add(cb.equal(memoire.get("type"), type));
        }
        if (ufr != null && !ufr.equals("Tous")) {  // Ignorer si "Tous" est sélectionné
            predicates.add(cb.equal(memoire.get("ufr"), ufr));
        }
        if (departement != null && !departement.equals("Tous")) {  // Ignorer si "Tous" est sélectionné
            predicates.add(cb.equal(memoire.get("departement"), departement));
        }
        if (filiere != null && !filiere.equals("Tous")) {  // Ignorer si "Tous" est sélectionné
            predicates.add(cb.equal(memoire.get("filiere"), filiere));
        }

        // Appliquer les filtres avec "and" (tous les critères doivent correspondre)
        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

    public List<Memoire> rechercherParMotsCles(String motsCles) {
        Specification<Memoire> spec = MemoireSpecifications.withMotsCles(motsCles);
        return memoireRepository.findAll(spec);
    }

}