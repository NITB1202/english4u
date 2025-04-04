package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetDetailResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetsPaginationResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface VocabularySetService {
    Mono<VocabularySetDetailResponseDto> createVocabularySet(UUID userId, CreateVocabularySetRequestDto request);
    Mono<VocabularySetResponseDto> getVocabularySetById(UUID id);
    Mono<VocabularySetsPaginationResponseDto> getVocabularySets(int page, int size);
    Mono<VocabularySetsPaginationResponseDto> searchVocabularySetByName(String keyword, int page, int size);
    Mono<VocabularySetResponseDto> updateVocabularySet(UUID id, UUID userId, UpdateVocabularySetRequestDto request);
    Mono<VocabularySetResponseDto> deleteVocabularySet(UUID id, UUID userId);
}
