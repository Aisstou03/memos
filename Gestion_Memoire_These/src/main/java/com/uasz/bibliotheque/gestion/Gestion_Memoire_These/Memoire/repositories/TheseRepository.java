package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheseRepository extends JpaRepository<These, Long>, JpaSpecificationExecutor<These> {
    @Query("SELECT COUNT(t) FROM These t")
    long countTheses();

    // Rechercher les thèses par nom de l'UFR de l'école doctorale
    List<These> findByEcoleDoctoratUfrNom(String ufrNom);
    // Trouver toutes les thèses marquées comme supprimées (dans la corbeille)
    List<These> findByEstSupprime(Boolean estSupprime);

    // Trouver une thèse par son ID (pratique pour les actions de restauration et suppression)
    Optional<These> findById(Long id);

    // Trouver toutes les thèses qui ne sont pas supprimées
    List<These> findByEstSupprimeFalse();

    @Query("SELECT COUNT(t) FROM These t WHERE t.estSupprime = false")
    long countNonSupprimeTheses();

    @Query("SELECT t FROM These t WHERE t.estSupprime = false")
    List<These> findAllNotDeleted();


}