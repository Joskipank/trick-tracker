package com.tricktracker.authservice.repository;

import com.tricktracker.authservice.entity.CredentialsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthRepository extends JpaRepository<CredentialsEntity, UUID> {
    Optional<CredentialsEntity> findByEmail(String email);

    boolean existsByEmail(String Email);
}
