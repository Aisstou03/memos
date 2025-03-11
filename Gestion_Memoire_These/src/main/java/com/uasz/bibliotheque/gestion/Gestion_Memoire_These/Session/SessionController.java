package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private SessionManager sessionManager;

    @GetMapping
    public String listSessions(Model model) {
        List<SessionInformation> sessions = sessionManager.getActiveSessions();
        model.addAttribute("sessions", sessions);
        return "sessions";
    }

    @PostMapping("/terminate")
    public String terminateSession(@RequestParam String sessionId) {
        sessionManager.expireSession(sessionId);
        return "redirect:/sessions?terminated=true";
    }
}
