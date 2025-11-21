package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele;

import com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.modele.Utilisateur;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(targetEntity = Utilisateur.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Utilisateur user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private boolean used = false;

    // Constructeur par défaut
    public PasswordResetToken() {
        this.createdDate = LocalDateTime.now();
    }

    // Constructeur avec paramètres
    public PasswordResetToken(String token, Utilisateur user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.createdDate = LocalDateTime.now();
        this.used = false;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    // Méthode pour vérifier si le token a expiré
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    // Méthode pour vérifier si le token est valide (non expiré et non utilisé)
    public boolean isValid() {
        return !isExpired() && !isUsed();
    }
}