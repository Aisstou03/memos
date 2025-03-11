package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Filiere;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Ufr;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.UfrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Departement;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.DepartementRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;
    @Autowired
    private UfrRepository ufrRepository;

    // Récupérer tous les départements
    public List<Departement> findAllDepartements() {
        return departementRepository.findAll();
    }

    // Récupérer les départements par UFR
    public List<String> getDepartementsByUFR(String ufrNom) {
        return ufrRepository.findByNom(ufrNom)
                .map(ufr -> departementRepository.findByUfr(ufr)
                        .stream()
                        .map(Departement::getNom)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()); // Retourne une liste vide si l'UFR n'existe pas
    }
    public Optional<Departement> findByNom(String name) {
        return departementRepository.findByNom(name);
    }


}
