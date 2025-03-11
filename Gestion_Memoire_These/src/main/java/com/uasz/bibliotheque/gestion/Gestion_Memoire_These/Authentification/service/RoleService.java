package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.service;


import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Méthode pour ajouter un rôle, si le rôle existe déjà, il n'est pas recréé
    public Role ajouterRole(String roleName) {
        Role role = roleRepository.findRoleByRole(roleName);
        if (role == null) {
            role = new Role(roleName);
            roleRepository.save(role);
        }
        return role;
    }

    // Méthode pour rechercher un rôle par nom
    public Role findByNom(String roleName) {
        return roleRepository.findRoleByRole(roleName);
    }
}