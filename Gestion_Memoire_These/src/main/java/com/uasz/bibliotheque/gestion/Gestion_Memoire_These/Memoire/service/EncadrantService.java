package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Encadrant;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.EncadrantRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EncadrantService {
    @Autowired
    EncadrantRepository encadrantRepository;

    public List<Encadrant> findAll() {
        return encadrantRepository.findAll();
    }

    public Encadrant findById(Long id) {
        return encadrantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Supervisor not found with ID: " + id));
    }
    public Encadrant findByNom(String nom) {
        return encadrantRepository.findByNom(nom)
                .orElseThrow(() -> new IllegalArgumentException("Encadrant avec le nom " + nom + " non trouvé"));
    }
    // Sauvegarde d'un encadrant
    public Encadrant save(Encadrant encadrant) {
        return encadrantRepository.save(encadrant);  // Sauvegarde l'encadrant dans la base de données
    }

    public Encadrant createNewSupervisor(Encadrant supervisor) {
        return encadrantRepository.save(supervisor);
    }

}

