package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.repository;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.model.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, Long> {}