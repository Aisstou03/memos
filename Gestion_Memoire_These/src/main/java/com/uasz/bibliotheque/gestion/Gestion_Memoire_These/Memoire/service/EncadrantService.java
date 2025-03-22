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
    public Optional<Encadrant> findByNom(String name) {
        return encadrantRepository.findByNom(name);
    }

    public Encadrant createNewSupervisor(Encadrant supervisor) {
        return encadrantRepository.save(supervisor);
    }

}

