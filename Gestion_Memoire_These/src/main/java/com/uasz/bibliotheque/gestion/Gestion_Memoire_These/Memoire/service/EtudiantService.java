package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Etudiant;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.EtudiantRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EtudiantService {

    @Autowired
    EtudiantRepository etudiantRepository;
    public List<Etudiant> findAll() {
        return etudiantRepository.findAll();
    }


    public Etudiant findById(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
    }

    public Etudiant findByNom(String nom) {
        return etudiantRepository.findByNom(nom)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant avec le nom " + nom + " non trouvé"));
    }

    public Etudiant createNewStudent(Etudiant student) {
        return etudiantRepository.save(student);
    }
    // Sauvegarde d'un étudiant
    public Etudiant save(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);  // Sauvegarde l'étudiant dans la base de données
    }
}
