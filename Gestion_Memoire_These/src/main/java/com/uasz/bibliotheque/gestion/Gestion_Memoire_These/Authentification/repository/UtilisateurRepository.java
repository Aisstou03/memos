package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Query("SELECT u FROM Utilisateur u WHERE u.username = :username")
    Utilisateur findUtilisateurByUsername(@Param("username") String username);

    List<Utilisateur> findByRoles_Role(String role);
    // Vérifie si un utilisateur existe par son username (ou email)
    boolean existsByUsername(String username);

    Utilisateur findByUsername(String username);
    List<Utilisateur> findByIsOnline(boolean isOnline);



}
