package com.nitb.apigateway.service.UserVocabulary;

import com.nitb.apigateway.dto.Action.ActionResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateSavedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.request.UpdateSavedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetDetailResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetStateStatisticResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetsPaginationResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class SavedSetServiceImpl implements SavedSetService{
    @Override
    public Mono<SavedSetResponseDto> createSavedSet(UUID userId, CreateSavedSetRequestDto request) {
        return null;
    }

    @Override
    public Mono<SavedSetDetailResponseDto> getSavedSetById(UUID id) {
        return null;
    }

    @Override
    public Mono<SavedSetsPaginationResponseDto> getSavedSets(UUID userId, int page, int size) {
        return null;
    }

    @Override
    public Mono<SavedSetsPaginationResponseDto> searchSavedSets(UUID userId, String keyword, int page, int size) {
        return null;
    }

    @Override
    public Mono<SavedSetResponseDto> updateSavedSet(UUID id, UpdateSavedSetRequestDto request) {
        return null;
    }

    @Override
    public Mono<ActionResponseDto> deleteSavedSet(UUID id) {
        return null;
    }

    @Override
    public Mono<SavedSetStateStatisticResponseDto> getSavedSetStateStatistic(UUID userId) {
        return null;
    }
}
