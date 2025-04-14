package com.nitb.apigateway.service.UserVocabulary;

import com.nitb.apigateway.dto.Action.ActionResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateCachedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.request.UpdateCachedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetDetailResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public class CachedSetServiceImpl implements CachedSetService{
    @Override
    public Mono<CachedSetResponseDto> cacheSet(UUID userId, CreateCachedSetRequestDto request) {
        return null;
    }

    @Override
    public Mono<List<CachedSetDetailResponseDto>> getAllCachedSets(UUID userId) {
        return null;
    }

    @Override
    public Mono<ActionResponseDto> updateCachedSet(UUID id, UpdateCachedSetRequestDto request) {
        return null;
    }
}
