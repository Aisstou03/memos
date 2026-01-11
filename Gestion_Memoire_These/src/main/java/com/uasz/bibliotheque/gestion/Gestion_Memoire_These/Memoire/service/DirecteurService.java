package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Directeur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.DirecteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirecteurService {
    @Autowired
    private  DirecteurRepository directeurRepository;
    @PreAuthorize("hasRole('ADMIN')")
    public Directeur getDirecteur() {
        return directeurRepository.findById(1L)
                .orElse(new Directeur("Gora LO"));
    }

    public void updateDirecteur(String nom) {
        Directeur d = getDirecteur();
        d.setNom(nom);
        directeurRepository.save(d);
    }
}
