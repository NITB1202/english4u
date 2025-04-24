package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface VocabularySetService {
    Mono<VocabularySetWithWordsResponseDto> createVocabularySet(UUID userId, CreateVocabularySetRequestDto request);
    Mono<VocabularySetDetailResponseDto> getVocabularySetById(UUID id);
    Mono<VocabularySetsPaginationResponseDto> getVocabularySets(int page, int size);
    Mono<VocabularySetsPaginationResponseDto> searchVocabularySetByName(String keyword, int page, int size);
    Mono<UpdateVocabularySetResponseDto> updateVocabularySet(UUID id, UUID userId, UpdateVocabularySetRequestDto request);
    Mono<DeleteVocabularySetResponseDto> deleteVocabularySet(UUID id, UUID userId);
}
