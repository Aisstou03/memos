package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.EcoleDoctorat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcoleDoctoratRepository extends JpaRepository<EcoleDoctorat, Long> {
    // Méthode personnalisée pour vérifier si une école doctorale existe par son nom
    boolean existsByNom(String nom);
}