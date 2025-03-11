package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Etudiant;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByNom(String nom);

    Optional<Etudiant> findByNomAndPrenom(String nom, String prenom);

    @Query("SELECT e FROM Etudiant e WHERE CONCAT(e.nom, ' ', e.prenom) = :nomComplet")
    Optional<Etudiant> findByNomComplet(@Param("nomComplet") String nomComplet);

}
