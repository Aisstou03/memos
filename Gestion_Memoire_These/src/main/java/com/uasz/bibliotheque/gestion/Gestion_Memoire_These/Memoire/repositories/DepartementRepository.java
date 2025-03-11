package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Departement;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Ufr;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartementRepository  extends JpaRepository<Departement, Long> {
    Optional<Departement> findByNom(String nom);
    List<Departement> findByUfrId(Long ufrId);

    Optional<Departement> findByNomAndUfr(String nom, Ufr ufr);

    List<Departement> findByUfr(Ufr ufr);
    boolean existsByNomAndUfr(String nom, Ufr ufr);
}
