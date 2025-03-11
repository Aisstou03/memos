package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.controller.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
