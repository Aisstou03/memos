package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor // Conserve le constructeur sans argument pour JPA
public class Role {
    @Id
    private String role;

    // Constructeur avec un paramètre String
    public Role(String role) {
        this.role = role;
    }

}
