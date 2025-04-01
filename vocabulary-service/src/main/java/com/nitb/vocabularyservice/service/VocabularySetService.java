package com.nitb.vocabularyservice.service;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.CreateVocabularySetRequest;
import com.nitb.vocabularyservice.repository.VocabularySetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VocabularySetService {
    private final VocabularySetRepository vocabularySetRepository;

    public VocabularySet createVocabularySet(CreateVocabularySetRequest request) {
        if(vocabularySetRepository.existsByName(request.getName())) {
            throw new BusinessException("This name has been used already.");
        }

        UUID creatorId = UUID.fromString(request.getCreatorId());

        VocabularySet vocabularySet = VocabularySet.builder()
                .name(request.getName())
                .createdBy(creatorId)
                .createdAt(LocalDateTime.now())
                .updatedBy(creatorId)
                .updatedAt(LocalDateTime.now())
                .wordCount(0)
                .isDeleted(false)
                .build();

        return vocabularySetRepository.save(vocabularySet);
    }
}
