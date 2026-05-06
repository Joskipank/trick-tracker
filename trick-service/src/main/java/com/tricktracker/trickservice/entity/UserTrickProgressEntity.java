package com.tricktracker.trickservice.entity;

import com.tricktracker.trickservice.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "user_trick_progress",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "trick_id"})})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserTrickEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId; // UUID из user-service

    @Column(name = "trick_id", nullable = false)
    private UUID trickId; // UUID из текущего trick-service

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProgressStatus status;
}
