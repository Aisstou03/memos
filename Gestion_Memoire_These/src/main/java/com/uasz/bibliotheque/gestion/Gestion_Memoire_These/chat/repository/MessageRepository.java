package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.repository;


import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByOrderByDateEnvoiAsc();
}