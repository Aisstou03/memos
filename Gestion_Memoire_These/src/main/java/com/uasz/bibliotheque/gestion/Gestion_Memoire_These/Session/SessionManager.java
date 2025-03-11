package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionManager {

    @Autowired
    private SessionRegistry sessionRegistry;

    /**
     * Récupère toutes les sessions actives.
     */
    public List<SessionInformation> getActiveSessions() {
        return sessionRegistry.getAllPrincipals().stream()
                .map(principal -> sessionRegistry.getAllSessions(principal, false))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * Termine une session en fonction de l'ID de session.
     */
    public void expireSession(String sessionId) {
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if (sessionInformation != null) {
            sessionInformation.expireNow(); // Termine immédiatement la session
        }
    }
}

