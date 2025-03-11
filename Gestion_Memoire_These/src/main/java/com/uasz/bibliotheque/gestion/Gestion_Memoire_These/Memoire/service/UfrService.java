package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Departement;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Filiere;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Ufr;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.DepartementRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.FiliereRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.UfrRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UfrService {

    @Autowired
    private UfrRepository ufrRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    public void ajouterUfrDepartementFiliere(String nomUfr, String nomDepartement, String nomFiliere) {
        // Vérifier et insérer UFR
        Ufr ufr = ufrRepository.findByNom(nomUfr).orElseGet(() -> {
            Ufr newUfr = new Ufr();
            newUfr.setNom(nomUfr);
            return ufrRepository.save(newUfr);
        });

        // Vérifier et insérer Département
        Departement departement = departementRepository.findByNomAndUfr(nomDepartement, ufr).orElseGet(() -> {
            Departement newDepartement = new Departement();
            newDepartement.setNom(nomDepartement);
            newDepartement.setUfr(ufr);
            return departementRepository.save(newDepartement);
        });

        // Vérifier et insérer Filière
        filiereRepository.findByNomAndDepartement(nomFiliere, departement).orElseGet(() -> {
            Filiere newFiliere = new Filiere();
            newFiliere.setNom(nomFiliere);
            newFiliere.setDepartement(departement);
            return filiereRepository.save(newFiliere);
        });
    }

    // Récupérer tous les UFRs
    public List<Ufr> findAllUfrs() {
        return ufrRepository.findAll();
    }

    public Optional<Ufr> findByNom(String name) {
        return ufrRepository.findByNom(name);
    }


    // Récupérer les départements pour un UFR donné
    public List<Departement> findDepartementsByUfr(Ufr ufr) {
        return departementRepository.findByUfr(ufr);
    }
}
