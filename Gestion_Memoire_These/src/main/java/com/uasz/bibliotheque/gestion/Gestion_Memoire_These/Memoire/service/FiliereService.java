package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Departement;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Filiere;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Ufr;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.DepartementRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.FiliereRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.MemoireRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.UfrRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FiliereService {

    @Autowired
    private UfrRepository ufrRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private MemoireRepository memoireRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    public List<Ufr> getAllUfr() {
        return ufrRepository.findAll();
    }

    public List<Departement> getDepartementsByUfr(Long ufrId) {
        return departementRepository.findByUfrId(ufrId);
    }

    public Filiere getFiliereByNom(String nomFiliere) {
        return filiereRepository.findByNom(nomFiliere).orElse(null);
    }

    public List<Filiere> getFilieresByDepartement(Long departementId) {
        return filiereRepository.findByDepartementId(departementId);
    }

    public Filiere getFiliereByNomAndDepartement(String nom, Departement departement) {
        return filiereRepository.findByNomAndDepartement(nom, departement).orElse(null);
    }


    public List<Filiere> findAll() {
        return filiereRepository.findAll();
    }


    public Memoire findById(Long id) {
        return memoireRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Memoire not found with ID: " + id));
    }


    public Optional<Filiere> findByNom(String name) {
        return filiereRepository.findByNom(name);
    }

    public Filiere createNewField(Filiere field) {
        return filiereRepository.save(field);
    }


    public List<String> getFilieresByDepartement(String departementNom) {
        return departementRepository.findByNom(departementNom)
                .map(departement -> filiereRepository.findByDepartement(departement)
                        .stream()
                        .map(Filiere::getNom)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList()); // Retourne une liste vide si le département n'existe pas
    }

}
