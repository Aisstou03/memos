package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.TypeMemoire;
import org.springframework.data.jpa.domain.Specification;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;

public class MemoireSpecifications {

    public static Specification<Memoire> withCote(String cote) {
        return (root, query, criteriaBuilder) ->
                cote == null ? null : criteriaBuilder.equal(root.get("cote"), cote);
    }

    public static Specification<Memoire> withTitre(String titre) {
        return (root, query, criteriaBuilder) ->
                titre == null || titre.isEmpty() ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("titre")), "%" + titre.toLowerCase() + "%");
    }

    public static Specification<Memoire> withEtudiant(String etudiant) {
        return (root, query, criteriaBuilder) -> {
            if (etudiant == null) return null;

            String[] parts = etudiant.toLowerCase().split(" "); // Diviser par espaces
            if (parts.length == 1) {
                // Recherche sur nom ou prénom si un seul mot est fourni
                String pattern = "%" + parts[0] + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("etudiant").get("nom")), pattern)
                );
            } else {
                // Recherche par combinaison de nom et prénom
                String nomPattern = "%" + parts[0] + "%";
                String prenomPattern = "%" + parts[1] + "%";
                return criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("etudiant").get("nom")), nomPattern)
                );
            }
        };
    }

    public static Specification<Memoire> withEncadrant(String encadrant) {
        return (root, query, criteriaBuilder) -> {
            if (encadrant == null) return null;

            String[] parts = encadrant.toLowerCase().split(" "); // Diviser par espaces
            if (parts.length == 1) {
                // Recherche sur nom ou prénom si un seul mot est fourni
                String pattern = "%" + parts[0] + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("encadrant").get("nom")), pattern)
                );
            } else {
                // Recherche par combinaison de nom et prénom
                String nomPattern = "%" + parts[0] + "%";
                String prenomPattern = "%" + parts[1] + "%";
                return criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("encadrant").get("nom")), nomPattern)
                );
            }
        };
    }

    public static Specification<Memoire> withAnnee(Integer annee) {
        return (root, query, criteriaBuilder) ->
                annee == null ? null : criteriaBuilder.equal(root.get("annee"), annee);
    }

    public static Specification<Memoire> withFiliere(String filiere) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("filiere").get("nom"), filiere);
    }
    public static Specification<Memoire> withType(TypeMemoire type) {
        return (root, query, criteriaBuilder) ->
                type != null ? criteriaBuilder.equal(root.get("type"), type) : criteriaBuilder.conjunction();
    }

    public static Specification<These> withThese() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get("ecoleDoctorat"));
    }



    public static Specification<Memoire> withUFR(String ufrNom) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("filiere").get("departement").get("ufr").get("nom"), ufrNom);
    }

    public static Specification<Memoire> withDepartement(String departementNom) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("filiere").get("departement").get("nom"), departementNom);
    }

}
