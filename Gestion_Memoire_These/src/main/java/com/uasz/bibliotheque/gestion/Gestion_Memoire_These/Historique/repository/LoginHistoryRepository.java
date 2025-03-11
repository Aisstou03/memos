package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.repository;


import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.model.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {}