package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.Memoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.These;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.TypeMemoire;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.repositories.TheseRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.MemoireSpecifications;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.utils.TheseSpecifications;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TheseService {
    @Autowired
    private TheseRepository theseRepository;
    // Méthode pour récupérer une thèse par son ID
    public These getThesisById(Long id) {
        return theseRepository.findById(id).orElse(null); // Retourne la thèse si elle existe, sinon null
    }

    // Méthode pour mettre à jour une thèse
    public void updateThesis(Long id, These updatedThesis) {
        These these = theseRepository.findById(id).orElse(null);
        if (these != null) {
            these.setCote(updatedThesis.getCote());
            these.setTitre(updatedThesis.getTitre());
            these.setEtudiant(updatedThesis.getEtudiant());
            these.setEncadrant(updatedThesis.getEncadrant());
            these.setAnnee(updatedThesis.getAnnee());
            these.setExemplaires(updatedThesis.getExemplaires());
            theseRepository.save(these); // Sauvegarde la thèse mise à jour
        }
    }

    // Méthode pour supprimer une thèse
    public void deleteThesis(Long id) {
        theseRepository.deleteById(id); // Supprime la thèse par son ID
    }

    public List<These> getAllThese() {
        return theseRepository.findAll();
    }

    //filtre theses
    public List<These> getMemoiresTheseFiltres(String ufrNom, String ecoleDoctoratNom) {
        Specification<These> spec = Specification.where(null); // Initialise une spécification vide

        if (ufrNom != null) spec = spec.and(TheseSpecifications.withUFR(ufrNom));
        if (ecoleDoctoratNom != null) spec = spec.and(TheseSpecifications.withEcoleDoctorat(ecoleDoctoratNom));

        return theseRepository.findAll(spec); // Utilise theseRepository (et non memoireRepository)
    }

    public List<These> searchMemos(Specification<These> spec) {
        return theseRepository.findAll(spec);
    }


}
