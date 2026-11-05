package com.tricktracker.trickservice.service.impl;

import com.tricktracker.trickservice.dto.request.UpdateTrickNotesRequest;
import com.tricktracker.trickservice.dto.request.UpdateTrickProgressRequest;
import com.tricktracker.trickservice.dto.response.TrickCoverImageResponse;
import com.tricktracker.trickservice.dto.response.TrickDetailsResponse;
import com.tricktracker.trickservice.entity.TrickEntity;
import com.tricktracker.trickservice.entity.UserTrickProgressEntity;
import com.tricktracker.trickservice.enums.ProgressStatus;
import com.tricktracker.trickservice.enums.TrickType;
import com.tricktracker.trickservice.exception.ResourceNotFoundException;
import com.tricktracker.trickservice.repository.TrickRepository;
import com.tricktracker.trickservice.repository.UserTrickProgressRepository;
import com.tricktracker.trickservice.service.UserTrickProgressService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional; // Важно: используем спринговую аннотацию
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserTrickProgressServiceImpl implements UserTrickProgressService {
    private final UserTrickProgressRepository progressRepository;
    private final TrickRepository trickRepository;

    @Override
    @Transactional(readOnly = true)
    public TrickDetailsResponse getTrickDetails(UUID userId, UUID trickId) {
        TrickEntity trick = trickRepository.findById(trickId)
                .orElseThrow(() -> new ResourceNotFoundException("Trick not found", "404"));

        // Применяем ваш принцип: если прогресса нет, создаем "пустой" объект в памяти
        UserTrickProgressEntity progress = progressRepository
                .findByUserIdAndTrickId(userId, trickId)
                .orElseGet(() -> createProgress(userId, trickId)); // Оптимизировали: передаем уже найденный trick

        return toDetailsResponse(trick, progress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrickCoverImageResponse> getTricksByType(UUID userId, TrickType type) {
        List<TrickEntity> tricks = trickRepository.findAllByType(type);

        // Группируем прогресс сразу в Map (trickId -> progress)
        Map<UUID, UserTrickProgressEntity> progressMap = progressRepository.findAllByUserId(userId)
                .stream()
                .collect(Collectors.toMap(
                        progress -> progress.getTrick().getId(),
                        progress -> progress
                ));

        return tricks.stream()
                .map(trick -> toResponse(trick, progressMap.get(trick.getId())))
                .sorted(Comparator.comparing(this::getSortingPriority))
                .toList();
    }

    @Override
    public UserTrickProgressEntity changeProgressStatus(UUID userId, UUID trickId) {

        UserTrickProgressEntity progress = progressRepository
                .findByUserIdAndTrickId(userId, trickId)
                .orElseGet(() -> createProgress(userId, trickId));

        progress.setProgressStatus(ProgressStatus.IN_PROGRESS);

        return progressRepository.save(progress);
    }

    @Override
    public UserTrickProgressEntity addToFavorite(UUID userId, UUID trickId) {

        UserTrickProgressEntity progress = progressRepository
                .findByUserIdAndTrickId(userId, trickId)
                .orElseGet(() -> createProgress(userId, trickId));

        progress.setFavorite(true);

        return progressRepository.save(progress);
    }

    private TrickDetailsResponse toDetailsResponse(TrickEntity trick, UserTrickProgressEntity progress) {
        boolean isFavorite = progress.isFavorite();
        ProgressStatus status = progress.getProgressStatus();

        return TrickDetailsResponse.builder()
                .name(trick.getName())
                .description(trick.getDescription())
                .avatarSvg(trick.getAvatarSvg())
                .videoUrl(trick.getVideoUrl())
                .skateLevel(trick.getSkateLevel())
                .trickType(trick.getTrickType())
                .isFavorite(isFavorite)
                .progressStatus(status)
                .build();
    }

    // Выносим тяжелый маппинг в отдельный читаемый метод
    private TrickCoverImageResponse toResponse(TrickEntity trick, UserTrickProgressEntity progress) {
        boolean isFavorite = progress != null && progress.isFavorite();
        ProgressStatus status = progress != null ? progress.getProgressStatus() : ProgressStatus.NOT_STARTED;

        return TrickCoverImageResponse.builder()
                .avatarUrl(trick.getAvatarSvg())
                .name(trick.getName())
                .skateLevel(trick.getSkateLevel())
                .isFavorite(isFavorite)
                .progressStatus(status)
                .build();
    }

    // Упрощаем логику сортировки: сначала избранное и в процессе (приоритет 0), затем всё остальное (приоритет 1)
    private int getSortingPriority(TrickCoverImageResponse response) {
        boolean isHighPriority = response.isFavorite();
        return isHighPriority ? 0 : 1;
    }

    private UserTrickProgressEntity createProgress(UUID userId, UUID trickId) {

        TrickEntity trick = trickRepository.findById(trickId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Trick not found: " + trickId
                        )
                );

        return UserTrickProgressEntity.builder()
                .userId(userId)
                .trick(trick)
                .progressStatus(ProgressStatus.NOT_STARTED)
                .isFavorite(false)
                .build();
    }
}
