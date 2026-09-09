package com.tricktracker.userservice.repository;

import com.tricktracker.userservice.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, UUID> {
    //Проверяет, занят ли уже такой username.
    boolean existsByUsername(String username);



    //Ищет пользователя по точному username
    Optional<UserProfileEntity> findByUsername(String username);

    //Проверяет, существует ли уже пользователь с таким номером телефона.
    boolean existsByPhone(String phone);

    Optional<UserProfileEntity> findByEmail(String email);
}
