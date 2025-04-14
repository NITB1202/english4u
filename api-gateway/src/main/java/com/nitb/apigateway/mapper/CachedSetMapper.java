package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetDetailResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetResponseDto;
import com.nitb.uservocabularyservice.grpc.CachedSetResponse;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class CachedSetMapper {
    private CachedSetMapper(){}

    public static CachedSetResponseDto toCachedSetResponseDto(CachedSetResponse set){
        return CachedSetResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .userId(UUID.fromString(set.getUserId()))
                .setId(UUID.fromString(set.getSetId()))
                .learnedWords(set.getLearnedWords())
                .lastAccess(LocalDateTime.parse(set.getLastAccess()))
                .build();
    }

    public static CachedSetDetailResponseDto toCachedSetDetailResponseDto(CachedSetResponse cachedSet, VocabularySetResponse set){
        return CachedSetDetailResponseDto.builder()
                .id(UUID.fromString(cachedSet.getId()))
                .userId(UUID.fromString(cachedSet.getUserId()))
                .set(VocabularySetMapper.toVocabularySetResponseDto(set))
                .learnedWords(cachedSet.getLearnedWords())
                .lastAccess(LocalDateTime.parse(cachedSet.getLastAccess()))
                .build();
    }
}
