package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import org.springframework.data.jpa.repository.JpaRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.TypeMemoire;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoireRepository extends JpaRepository<Memoire, Long>, JpaSpecificationExecutor<Memoire> {

    List<Memoire> findByType(TypeMemoire typeMemoire);
    @Query("SELECT m.annee, COUNT(m) FROM Memoire m GROUP BY m.annee ORDER BY m.annee ASC")
    List<Object[]> countMemosGroupedByYear();

    List<Memoire> findAllByType(TypeMemoire type);

    @Query("SELECT m FROM Memoire m WHERE m.annee = :annee AND m.type = :type")
    List<Memoire> findByAnneeAndType(@Param("annee") int annee, @Param("type") TypeMemoire type);

    //liste des memoires se trouvant dans la corbeille
    List<Memoire> findByCorbeilleTrue();

    // Recherche les titres qui commencent par le mot saisi
    @Query("SELECT m.titre FROM Memoire m WHERE LOWER(m.titre) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    List<String> findSuggestions(@Param("motCle") String motCle);

    @Query("SELECT m FROM Memoire m WHERE m.corbeille = false")
    List<Memoire> findMemoiresActifs(); // Récupère les mémoires actifs (non supprimés)

    List<Memoire> findByCorbeilleFalse(); // Alternative : Spring Data JPA comprend automatiquement cette requête
    

}
