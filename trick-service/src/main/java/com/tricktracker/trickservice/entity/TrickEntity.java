package com.tricktracker.trickservice.entity;

import com.tricktracker.trickservice.enums.TrickCategory;
import com.tricktracker.trickservice.enums.SkateLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "tricks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TrickEntity {
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

    @Column(name = "trick_name", nullable = false, unique = true)
    private String name;

    @Column(name = "bio_trick", length = 1000)
    private String description;

    @Column(name = "tutorial_url")
    private String tutorialUrl;

    @Column(name = "avatar_svg", columnDefinition = "TEXT")
    private String avatarSvg;

    @Enumerated(EnumType.STRING)
    @Column(name = "skate_level", nullable = false)
    private SkateLevel skateLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "trick_category", nullable = false)
    private TrickCategory trickCategory;
}
