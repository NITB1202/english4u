package com.nitb.vocabularyservice.service;

import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.CreateVocabularySetRequest;
import com.nitb.vocabularyservice.repository.VocabularySetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VocabularySetService {
    private final VocabularySetRepository vocabularySetRepository;

    public VocabularySet createVocabularySet(CreateVocabularySetRequest request) {
        VocabularySet vocabularySet = VocabularySet.builder()
                .name(request.getName())
                .build();

        return vocabularySetRepository.save(vocabularySet);
    }
}
