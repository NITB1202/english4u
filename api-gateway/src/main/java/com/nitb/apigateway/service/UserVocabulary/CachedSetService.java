package com.nitb.apigateway.service.UserVocabulary;

import com.nitb.apigateway.dto.Action.ActionResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateCachedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.request.UpdateCachedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetDetailResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CachedSetService {
    Mono<CachedSetResponseDto> cacheSet(UUID userId, CreateCachedSetRequestDto request);
    Mono<List<CachedSetDetailResponseDto>> getAllCachedSets(UUID userId);
    Mono<ActionResponseDto> updateCachedSet(UUID id, UpdateCachedSetRequestDto request);
}
