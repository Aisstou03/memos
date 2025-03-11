package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.EcoleDoctorat;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheseRepository extends JpaRepository<These, Long>, JpaSpecificationExecutor<These> {
    @Query("SELECT COUNT(t) FROM These t")
    long countTheses();

    // Rechercher les thèses par nom de l'UFR de l'école doctorale
    List<These> findByEcoleDoctoratUfrNom(String ufrNom);

}
