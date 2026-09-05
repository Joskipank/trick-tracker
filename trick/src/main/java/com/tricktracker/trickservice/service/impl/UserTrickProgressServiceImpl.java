//package com.tricktracker.trickservice.service.impl;
//
//import com.tricktracker.trickservice.dto.request.UpdateTrickNotesRequest;
//import com.tricktracker.trickservice.dto.request.UpdateTrickProgressRequest;
//import com.tricktracker.trickservice.dto.response.TrickCoverImageResponse;
//import com.tricktracker.trickservice.dto.response.TrickProgressResponse;
//import com.tricktracker.trickservice.entity.TrickEntity;
//import com.tricktracker.trickservice.entity.UserTrickProgressEntity;
//import com.tricktracker.trickservice.enums.ProgressStatus;
//import com.tricktracker.trickservice.enums.SkateLevel;
//import com.tricktracker.trickservice.exception.ResourceNotFoundException;
//import com.tricktracker.trickservice.repository.TrickRepository;
//import com.tricktracker.trickservice.repository.UserTrickProgressRepository;
//import com.tricktracker.trickservice.service.UserTrickProgressService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class UserTrickProgressServiceImpl implements UserTrickProgressService {
//
//    private final UserTrickProgressRepository userTrickProgressRepository;
//
//    @Override
//   @Transactional(readOnly = true)
//    public TrickProgressResponse getMyTricks(UUID id) {
//       TrickEntity trickEntity = userTrickProgressRepository.findById(id)
//               .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", "Profile not found"));
//
//       return null;
//
//    }
//
//    @Override
//    public TrickProgressResponse updateTrick(UpdateTrickProgressRequest request) {
//        return null;
//    }
//
//    @Override
//    public TrickProgressResponse createJournalEntry(UpdateTrickNotesRequest request) {
//        return null;
//    }
//}
