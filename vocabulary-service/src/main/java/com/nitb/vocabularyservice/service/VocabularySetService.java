package com.nitb.vocabularyservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.*;
import com.nitb.vocabularyservice.repository.VocabularySetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularySetService {
    private final VocabularySetRepository vocabularySetRepository;

    private final int DEFAULT_SIZE = 10;

    public VocabularySet createVocabularySet(CreateVocabularySetRequest request) {
        if(vocabularySetRepository.existsByName(request.getName())) {
            throw new BusinessException("This name has been used already.");
        }

        UUID userId = UUID.fromString(request.getUserId());

        VocabularySet vocabularySet = VocabularySet.builder()
                .name(request.getName())
                .createdBy(userId)
                .createdAt(LocalDateTime.now())
                .updatedBy(userId)
                .updatedAt(LocalDateTime.now())
                .wordCount(0)
                .isDeleted(false)
                .build();

        return vocabularySetRepository.save(vocabularySet);
    }

    public VocabularySet getVocabularySetById(GetVocabularySetByIdRequest request) {
        UUID id  = UUID.fromString(request.getId());
        return vocabularySetRepository.findById(id).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + id)
        );
    }

    public Page<VocabularySet> getVocabularySets(GetVocabularySetsRequest request) {
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularySetRepository.findAll(PageRequest.of(page, size));
    }

    public Page<VocabularySet> searchVocabularySetByName(SearchVocabularySetByNameRequest request){
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() > 0 ? request.getSize() : DEFAULT_SIZE;

        return vocabularySetRepository.findByNameContainingIgnoreCase(request.getKeyword(), PageRequest.of(page, size));
    }

    public VocabularySet updateVocabularySet(UpdateVocabularySetRequest request) {
        UUID setId = UUID.fromString(request.getId());

        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + request.getId())
        );

        if(vocabularySetRepository.existsByName(request.getName())) {
            throw new BusinessException("This name has been used already.");
        }

        set.setName(request.getName());
        set.setUpdatedBy(UUID.fromString(request.getUserId()));
        set.setUpdatedAt(LocalDateTime.now());

        return vocabularySetRepository.save(set);

    }

    public VocabularySet deleteVocabularySet(DeleteVocabularySetRequest request){
        UUID setId = UUID.fromString(request.getId());

        VocabularySet set = vocabularySetRepository.findById(setId).orElseThrow(
                () -> new BusinessException("No vocabulary set found with id: " + request.getId())
        );

        set.setIsDeleted(true);
        set.setUpdatedBy(UUID.fromString(request.getUserId()));
        set.setUpdatedAt(LocalDateTime.now());

        return vocabularySetRepository.save(set);
    }
}
