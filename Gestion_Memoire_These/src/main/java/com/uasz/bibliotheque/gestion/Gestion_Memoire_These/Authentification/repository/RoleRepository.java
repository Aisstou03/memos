package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.repository;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, String> {
    @Query("SELECT r FROM Role r WHERE r.role = :role")
    Role findRoleByRole(@Param("role") String role);
}
