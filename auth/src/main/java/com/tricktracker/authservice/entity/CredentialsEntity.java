package com.tricktracker.authservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CredentialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = true;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true; //Если false, Spring Security автоматически запретит вход

    public CredentialsEntity(String email, String passwordHash){
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
