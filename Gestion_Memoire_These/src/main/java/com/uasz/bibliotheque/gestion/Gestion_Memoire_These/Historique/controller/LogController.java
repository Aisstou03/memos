package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.controller;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.repository.ActionLogRepository;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.repository.LoginHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {
    @Autowired
    private  LoginHistoryRepository loginHistoryRepository;
    @Autowired
    private  ActionLogRepository actionLogRepository;

    public LogController(LoginHistoryRepository loginHistoryRepository, ActionLogRepository actionLogRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.actionLogRepository = actionLogRepository;
    }

    @GetMapping("/logs")
    public String viewLogs(Model model) {
        model.addAttribute("loginHistories", loginHistoryRepository.findAll());
        model.addAttribute("actionLogs", actionLogRepository.findAll());
        return "logs";
    }
}