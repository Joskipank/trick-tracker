package com.tricktracker.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CredentialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive; //Если false, Spring Security автоматически запретит вход

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;
}
