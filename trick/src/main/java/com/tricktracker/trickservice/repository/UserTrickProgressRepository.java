package com.tricktracker.trickservice.repository;

import com.tricktracker.trickservice.entity.TrickEntity;
import com.tricktracker.trickservice.entity.UserTrickProgressEntity;
import com.tricktracker.trickservice.enums.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTrickProgressRepository extends JpaRepository<UserTrickProgressEntity, UUID> {
    Optional<UserTrickProgressEntity> findByUserIdAndTrickId(UUID userId, UUID trickId); // найти прогресс по user + trick

    boolean existsByUserIdAndTrickId(UUID userId, UUID trickId); // проверить существование (очень полезно)

    List<UserTrickProgressEntity> findAllByUserId(UUID userId); // все прогрессы пользователя

    @Query("SELECT p FROM UserTrickProgressEntity p JOIN FETCH p.trick WHERE p.userId = :userId AND p.trick.id = :trickId")
    Optional<UserTrickProgressEntity> findByUserIdAndTrickIdWithTrick(
            @Param("userId") UUID userId,
            @Param("trickId") UUID trickId
    );

    List<UserTrickProgressEntity> findAllByUserIdAndIsFavoriteTrue(UUID userId);

    List<UserTrickProgressEntity> findAllByTrickId(UUID trickId); // все пользователи по конкретному трюку (например, статистика)

    List<UserTrickProgressEntity> findAllByUserIdAndStatus(UUID userId, ProgressStatus status); // фильтр по статусу

    void deleteByUserIdAndTrickId(UUID userId, UUID trickId); // удалить прогресс (если нужно)
}