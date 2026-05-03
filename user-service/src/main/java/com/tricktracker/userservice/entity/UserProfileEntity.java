package com.tricktracker.userservice.entity;

import com.tricktracker.userservice.enums.AccountStatus;
import com.tricktracker.userservice.enums.PresenceStatus;
import com.tricktracker.userservice.enums.SkateLevel;
import com.tricktracker.userservice.enums.VisibilityLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Collate;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserProfileEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = now();
        updatedAt = now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = now();
    }

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "bio", length = 1000)
    private String bio;

    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @Column(name = "avatar_svg", columnDefinition = "TEXT")
    private String avatarSvg;

    @Enumerated(EnumType.STRING)
    @Column(name = "skate_level", nullable = false)
    @Builder.Default
    private SkateLevel level = SkateLevel.BEGINNER;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_visibility", nullable = false)
    @Builder.Default
    private VisibilityLevel phoneVisibility = VisibilityLevel.HIDDEN;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "presence_status", nullable = false)
    private PresenceStatus presenceStatus;
}
