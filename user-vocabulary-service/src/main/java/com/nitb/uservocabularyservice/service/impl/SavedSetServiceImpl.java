package com.nitb.uservocabularyservice.service.impl;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.uservocabularyservice.entity.SavedSet;
import com.nitb.uservocabularyservice.grpc.*;
import com.nitb.uservocabularyservice.repository.SavedSetRepository;
import com.nitb.uservocabularyservice.service.SavedSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavedSetServiceImpl implements SavedSetService {
    private final SavedSetRepository savedSetRepository;
    private final int DEFAULT_SIZE = 10;

    @Override
    public SavedSet createSavedSet(CreateSavedSetRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID setId = UUID.fromString(request.getSetId());

        if(savedSetRepository.existsByUserIdAndSetId(userId, setId)) {
            throw new BusinessException("This set has been saved.");
        }

        SavedSet set = SavedSet.builder()
                .userId(userId)
                .setId(setId)
                .learnedWords(request.getLearnedWords())
                .lastAccess(LocalDateTime.now())
                .build();

        return savedSetRepository.save(set);
    }

    @Override
    public SavedSet getSavedSetById(GetSavedSetByIdRequest request) {
        return savedSetRepository.findById(UUID.fromString(request.getId())).orElseThrow(() -> new NotFoundException("Saved set not found."));
    }

    @Override
    public Page<SavedSet> getSavedSets(GetSavedSetsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return savedSetRepository.findByUserId(UUID.fromString(request.getUserId()), PageRequest.of(page, size));
    }

    @Override
    public List<SavedSet> getAllSavedSets(GetAllSavedSetsRequest request) {
        return savedSetRepository.findByUserId(UUID.fromString(request.getUserId()));
    }

    @Override
    public SavedSet updateSavedSet(UpdateSavedSetRequest request) {
        SavedSet set = savedSetRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                () -> new NotFoundException("Saved set not found.")
        );

        set.setLearnedWords(Math.max(request.getLearnedWords(), set.getLearnedWords()));
        set.setLastAccess(LocalDateTime.now());

        return savedSetRepository.save(set);
    }

    @Override
    public void deleteSavedSet(DeleteSavedSetRequest request) {
        UUID id = UUID.fromString(request.getId());

        if(!savedSetRepository.existsById(id)) {
            throw new NotFoundException("Saved set not found.");
        }

        savedSetRepository.deleteById(id);
    }
}
