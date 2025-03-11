package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.EcoleDoctorat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EcoleDoctoraleRepository extends JpaRepository<EcoleDoctorat, Long> {
    Optional<EcoleDoctorat> findByNom(String nom);
}
