package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service;


import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UtilisateurDetailService implements UserDetailsService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public UtilisateurDetailService(UtilisateurRepository utilisateurRepository){
        this.utilisateurRepository = utilisateurRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur user = utilisateurRepository.findUtilisateurByUsername(username);
        String[] roles = user.getRoles().stream().map(u->u.getRole()).toArray(String []::new);
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(roles)
                        .build();
        return userDetails ;
    }
}
