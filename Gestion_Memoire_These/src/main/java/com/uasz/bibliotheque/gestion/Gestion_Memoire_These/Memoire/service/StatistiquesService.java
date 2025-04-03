package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.model.TypeMemoire;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;

@Service
public class StatistiquesService {
    private static final Logger logger = LoggerFactory.getLogger(StatistiquesService.class);
    private final MemoireService memoireService;

    public StatistiquesService(MemoireService memoireService) {
        this.memoireService = memoireService;
    }

    public void ajouterStatistiques(Model model) {
        try {
            logger.info("Récupération des statistiques des mémoires et thèses non supprimés.");

            // Compter uniquement les mémoires et thèses qui ne sont pas supprimés
            long licenceCount = memoireService.countMemosByTypeNonSupprime(TypeMemoire.LICENCE);
            long masterCount = memoireService.countMemosByTypeNonSupprime(TypeMemoire.MASTER);
            long doctoratCount = memoireService.countThesesNonSupprime();

            model.addAttribute("licenceCount", licenceCount);
            model.addAttribute("masterCount", masterCount);
            model.addAttribute("doctoratCount", doctoratCount);

            // Préparation des données pour le graphique
            List<Long> stats = List.of(licenceCount, masterCount, doctoratCount);
            model.addAttribute("stats", stats);

            // Récupération des statistiques par année et par type (seulement pour les mémoires/thèses non supprimés)
            Map<Integer, Long> licencesParAnnee = memoireService.countMemosByTypeAndYear(TypeMemoire.LICENCE);
            Map<Integer, Long> mastersParAnnee = memoireService.countMemosByTypeAndYear(TypeMemoire.MASTER);
            Map<Integer, Long> thesesParAnnee = memoireService.countThesesByYear();

            // Regrouper les années
            Set<Integer> anneesSet = new TreeSet<>();
            anneesSet.addAll(licencesParAnnee.keySet());
            anneesSet.addAll(mastersParAnnee.keySet());
            anneesSet.addAll(thesesParAnnee.keySet());

            List<Integer> annees = new ArrayList<>(anneesSet);

            // Préparer les données pour les graphiques
            List<Long> licencesCounts = annees.stream().map(year -> licencesParAnnee.getOrDefault(year, 0L)).collect(Collectors.toList());
            List<Long> mastersCounts = annees.stream().map(year -> mastersParAnnee.getOrDefault(year, 0L)).collect(Collectors.toList());
            List<Long> thesesCounts = annees.stream().map(year -> thesesParAnnee.getOrDefault(year, 0L)).collect(Collectors.toList());

            model.addAttribute("annees", annees);
            model.addAttribute("licencesCounts", licencesCounts);
            model.addAttribute("mastersCounts", mastersCounts);
            model.addAttribute("thesesCounts", thesesCounts);

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des statistiques : ", e);
            model.addAttribute("errorMessage", "Une erreur est survenue lors de la récupération des statistiques.");
        }
    }
}
