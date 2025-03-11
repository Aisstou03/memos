package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.listener;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.model.LoginHistory;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.repository.LoginHistoryRepository;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthenticationLoggingListener {

    private final LoginHistoryRepository loginHistoryRepository;

    public AuthenticationLoggingListener(LoginHistoryRepository loginHistoryRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
    }

    @EventListener
    public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        String ipAddress = ((WebAuthenticationDetails) event.getAuthentication().getDetails()).getRemoteAddress();

        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUsername(username);
        loginHistory.setIpAddress(ipAddress);
        loginHistory.setLoginTime(LocalDateTime.now());

        loginHistoryRepository.save(loginHistory);
    }
}