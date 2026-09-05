package com.tricktracker.trickservice.repository;

import com.tricktracker.trickservice.entity.TrickEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TrickRepository extends JpaRepository<TrickEntity, UUID> {
    Optional<TrickEntity> findBySlug(String slug); // поиск по slug
}
