package com.nitb.apigateway.service.Vocabulary;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.request.UpdateVocabularySetRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.*;
import com.nitb.common.enums.GroupBy;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface VocabularySetService {
    Mono<CreateVocabularySetResponseDto> createVocabularySet(UUID userId, CreateVocabularySetRequestDto request);
    Mono<VocabularySetDetailResponseDto> getVocabularySetById(UUID id);
    Mono<VocabularySetsPaginationResponseDto> getVocabularySets(int page, int size);
    Mono<VocabularySetsPaginationResponseDto> getDeletedVocabularySets(int page, int size);
    Mono<VocabularySetsPaginationResponseDto> searchVocabularySetByName(String keyword, int page, int size);
    Mono<VocabularySetsPaginationResponseDto> searchDeletedVocabularySetByName(String keyword, int page, int size);
    Mono<UpdateVocabularySetNameResponseDto> updateVocabularySetName(UUID id, UUID userId, String name);
    Mono<UpdateVocabularySetResponseDto> updateVocabularySet(UUID id, UUID userId, UpdateVocabularySetRequestDto request);
    Mono<DeleteVocabularySetResponseDto> deleteVocabularySet(UUID id, UUID userId);
    Mono<DeleteVocabularySetResponseDto> restoreVocabularySet(UUID id, UUID userId);
    Mono<VocabularySetStatisticsResponseDto> getVocabularySetStatistics(UUID userId, LocalDate from, LocalDate to, GroupBy groupBy);
}
