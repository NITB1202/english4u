package com.nitb.apigateway.service.UserVocabulary;

import com.nitb.apigateway.dto.General.DataWithMessageResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.request.CreateCachedSetRequestDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetSummaryResponseDto;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CachedSetService {
    Mono<DataWithMessageResponseDto> cacheSet(UUID userId, CreateCachedSetRequestDto request);
    Mono<List<CachedSetSummaryResponseDto>> getAllCachedSets(UUID userId);
}
