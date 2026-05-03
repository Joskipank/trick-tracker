package com.tricktracker.userservice.repository;

import com.tricktracker.userservice.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, UUID> {
    //Проверяет, занят ли уже такой username.
    boolean existsByUsername(String username);
    //Ищет пользователя по точному username
    Optional<UserProfileEntity> findByUsername(String username);

    //Проверяет, существует ли уже пользователь с таким номером телефона.
    boolean existsByPhone(String phone);

    Optional<UserProfileEntity> findByPhone(String phone);
}
