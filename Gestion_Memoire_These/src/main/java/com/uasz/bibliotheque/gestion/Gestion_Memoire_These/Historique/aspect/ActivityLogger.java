package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.aspect;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.model.ActionLog;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Historique.repository.ActionLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Aspect
@Component
public class ActivityLogger {

    private final ActionLogRepository actionLogRepository;

    public ActivityLogger(ActionLogRepository actionLogRepository) {
        this.actionLogRepository = actionLogRepository;
    }

    @AfterReturning("execution(* com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.controller.*.*(..))")
    public void logAction(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        ActionLog actionLog = new ActionLog();
        actionLog.setUsername(username);
        actionLog.setAction("Executed: " + methodName);
        actionLog.setActionDate(LocalDateTime.now());

        actionLogRepository.save(actionLog);
    }
}