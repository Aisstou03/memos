package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Ufr;

import java.util.Optional;

@Repository
public interface UfrRepository  extends JpaRepository<Ufr, Long> {
    Optional<Ufr> findByNom(String nom);
    boolean existsByNom(String nom);


}
