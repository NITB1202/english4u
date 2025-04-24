package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetSummaryResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.CachedSetResponseDto;
import com.nitb.uservocabularyservice.grpc.CachedSetResponse;
import com.nitb.uservocabularyservice.grpc.CachedSetSummaryResponse;
import com.nitb.vocabularyservice.grpc.VocabularySetDetailResponse;

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

    public static CachedSetSummaryResponseDto toCachedSetSummaryResponseDto(CachedSetSummaryResponse cachedSet, VocabularySetDetailResponse set){
        return CachedSetSummaryResponseDto.builder()
                .setId(UUID.fromString(cachedSet.getSetId()))
                .setName(set.getName())
                .wordCount(set.getWordCount())
                .learnedWords(cachedSet.getLearnedWords())
                .build();
    }
}
