package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.MotCle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotCleRepository  extends JpaRepository<MotCle, Long> {
    Optional<MotCle> findByValeur(String valeur);
}
