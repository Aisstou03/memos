package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(unique = true)
    private String username;//username reprsente l'email de l'utilisateur
    private String password;
    private String nom ;
    private String prenom ;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation = new Date();
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    private boolean isOnline = false; // Nouvel attribut pour l'état de connexion

}
