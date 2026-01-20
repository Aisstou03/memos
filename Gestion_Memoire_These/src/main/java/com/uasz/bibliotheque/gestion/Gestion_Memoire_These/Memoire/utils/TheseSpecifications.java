package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.EcoleDoctorat;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Ufr;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class TheseSpecifications {

    public static Specification<These> withCote(String cote) {
        return (root, query, criteriaBuilder) ->
                cote == null ? null : criteriaBuilder.equal(root.get("cote"), cote);
    }

    public static Specification<These> withTitre(String titre) {
        return (root, query, criteriaBuilder) ->
                titre == null || titre.isEmpty() ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("titre")), "%" + titre.toLowerCase() + "%");
    }

    public static Specification<These> withEtudiant(String etudiant) {
        return (root, query, criteriaBuilder) -> {
            if (etudiant == null) return null;

            String[] parts = etudiant.toLowerCase().split(" ");
            if (parts.length == 1) {
                String pattern = "%" + parts[0] + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("etudiant").get("nom")), pattern)
                );
            } else {
                String nomPattern = "%" + parts[0] + "%";
                String prenomPattern = "%" + parts[1] + "%";
                return criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("etudiant").get("nom")), nomPattern)
                );
            }
        };
    }


    public static Specification<These> withEncadrant(String encadrant) {
        return (root, query, criteriaBuilder) -> {
            if (encadrant == null) return null;

            String[] parts = encadrant.toLowerCase().split(" ");
            if (parts.length == 1) {
                String pattern = "%" + parts[0] + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("encadrant").get("nom")), pattern)
                );
            } else {
                String nomPattern = "%" + parts[0] + "%";
                String prenomPattern = "%" + parts[1] + "%";
                return criteriaBuilder.and(
                        criteriaBuilder.like(criteriaBuilder.lower(root.join("encadrant").get("nom")), nomPattern)
                );
            }
        };
    }

    public static Specification<These> withAnnee(Integer annee) {
        return (root, query, criteriaBuilder) ->
                annee == null ? null : criteriaBuilder.equal(root.get("annee"), annee);
    }

    public static Specification<These> withEcoleDoctorat(String ecoleDoctoratNom) {
        return (root, query, criteriaBuilder) -> {
            if (ecoleDoctoratNom == null) return null;
            Join<These, EcoleDoctorat> ecoleDoctoratJoin = root.join("ecoleDoctorat");
            return criteriaBuilder.like(criteriaBuilder.lower(ecoleDoctoratJoin.get("nom")), "%" + ecoleDoctoratNom.toLowerCase() + "%");
        };
    }

    public static Specification<These> withUFR(String ufrNom) {
        return (root, query, criteriaBuilder) -> {
            if (ufrNom == null) return null;
            Join<These, EcoleDoctorat> ecoleDoctoratJoin = root.join("ecoleDoctorat");
            Join<EcoleDoctorat, Ufr> ufrJoin = ecoleDoctoratJoin.join("ufr");
            return criteriaBuilder.like(criteriaBuilder.lower(ufrJoin.get("nom")), "%" + ufrNom.toLowerCase() + "%");
        };
    }
}
