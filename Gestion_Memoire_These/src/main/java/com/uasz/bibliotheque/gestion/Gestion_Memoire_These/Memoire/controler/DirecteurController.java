package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.controler;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Memoire.service.DirecteurService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class DirecteurController {
    @Autowired
    private DirecteurService directeurService;

    @GetMapping("/admin/directeur")
    public String pageDirecteur(Model model) {
        model.addAttribute("directeur", directeurService.getDirecteur());
        return "admin/directeurForm";
    }

    @PostMapping("/admin/directeur/save")
    public String saveDirecteur(@RequestParam String nom) {
        directeurService.updateDirecteur(nom);
        return "redirect:/memoires/liste";
    }
}

