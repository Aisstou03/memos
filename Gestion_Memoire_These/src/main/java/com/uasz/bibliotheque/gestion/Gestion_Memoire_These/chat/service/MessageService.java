package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.model.Message;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date; // Modifier cet import
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    @Autowired
    private  MessageRepository messageRepository;

    public List<Message> getMessages() {
        return messageRepository.findAllByOrderByDateEnvoiAsc();
    }

    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElse(null);
    }

    public Message envoyerMessage(Message message) {
        message.setDateEnvoi(new Date()); // Utiliser Date au lieu de LocalDateTime
        return messageRepository.save(message);
    }

    public void supprimerMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public Message modifierMessage(Message message) {
        return messageRepository.save(message);
    }
}