package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import org.springframework.stereotype.Repository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Departement;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Long> {

    // Rechercher une filière par son nom
    Optional<Filiere> findByNom(String nom);
    boolean existsByNomAndDepartement(String nom, Departement departement);
    List<Filiere> findByDepartementId(Long departementId);
    List<Filiere> findByDepartement(Departement departement);

    Optional<Filiere> findByNomAndDepartement(String nom, Departement departement);

}
