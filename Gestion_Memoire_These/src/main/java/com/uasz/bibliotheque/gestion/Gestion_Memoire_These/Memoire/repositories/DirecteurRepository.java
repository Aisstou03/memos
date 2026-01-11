package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Directeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirecteurRepository extends JpaRepository<Directeur, Long> {}
