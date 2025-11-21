package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.PasswordResetToken;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // Recherche un token par sa valeur
    Optional<PasswordResetToken> findByToken(String token);

    // Recherche tous les tokens d'un utilisateur
    List<PasswordResetToken> findByUser(Utilisateur user);

    // Recherche les tokens non expirés d'un utilisateur
    @Query("SELECT t FROM PasswordResetToken t WHERE t.user = ?1 AND t.expiryDate > ?2 AND t.used = false")
    List<PasswordResetToken> findValidTokensByUser(Utilisateur user, LocalDateTime now);

    // Supprime les tokens expirés
    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate < ?1")
    void deleteExpiredTokens(LocalDateTime now);

    // Supprime tous les tokens d'un utilisateur
    @Transactional
    @Modifying
    void deleteByUser(Utilisateur user);
}