package com.nitb.apigateway.service.UserVocabulary;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateSavedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.request.UpdateSavedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetStateStatisticResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetsPaginationResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SavedSetService {
    Mono<SavedSetResponseDto> createSavedSet(UUID userId, CreateSavedSetRequestDto request);
    Mono<SavedSetsPaginationResponseDto> getSavedSets(UUID userId, int page, int size);
    Mono<SavedSetsPaginationResponseDto> searchSavedSets(UUID userId, String keyword, int page, int size);
    Mono<SavedSetResponseDto> updateSavedSet(UUID id, UpdateSavedSetRequestDto request);
    Mono<ActionResponseDto> deleteSavedSet(UUID id);
    Mono<SavedSetStateStatisticResponseDto> getSavedSetStateStatistic(UUID userId);
}
