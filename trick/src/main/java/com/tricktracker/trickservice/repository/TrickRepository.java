package com.tricktracker.trickservice.repository;

import com.tricktracker.trickservice.entity.TrickEntity;
import com.tricktracker.trickservice.enums.TrickType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TrickRepository extends JpaRepository<TrickEntity, UUID> {
    List<TrickEntity> findAllByType(TrickType category);
}