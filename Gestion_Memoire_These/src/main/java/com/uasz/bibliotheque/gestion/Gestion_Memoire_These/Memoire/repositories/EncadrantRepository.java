package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Etudiant;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Encadrant;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Filiere;

import java.util.Optional;

@Repository
public interface EncadrantRepository extends JpaRepository<Encadrant, Long> {

    @Query("SELECT e FROM Encadrant e WHERE CONCAT(e.nom, ' ', e.prenom) = :nomComplet")
    Optional<Encadrant> findByNomComplet(@Param("nomComplet") String nomComplet);

    Optional<Encadrant> findByNom(String nom);

    Optional<Encadrant> findByNomAndPrenomAndFiliere(String nom, String prenom, Filiere filiere);
    Optional<Encadrant> findByNomAndPrenom(String nom, String prenom);


}
