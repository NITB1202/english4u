package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetSummaryResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetResponseDto;
import com.nitb.uservocabularyservice.grpc.SavedSetResponse;
import com.nitb.uservocabularyservice.grpc.SavedSetSummaryResponse;
import com.nitb.vocabularyservice.grpc.VocabularySetDetailResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class SavedSetMapper {
    private SavedSetMapper(){}

    public static SavedSetResponseDto toSavedSetResponseDto(SavedSetResponse set){
        return SavedSetResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .userId(UUID.fromString(set.getUserId()))
                .setId(UUID.fromString(set.getSetId()))
                .learnedWords(set.getLearnedWords())
                .lastAccess(LocalDateTime.parse(set.getLastAccess()))
                .build();
    }

    public static SavedSetSummaryResponseDto toSavedSetSummaryResponseDto(SavedSetSummaryResponse savedSet, VocabularySetDetailResponse set){
        return SavedSetSummaryResponseDto.builder()
                .id(UUID.fromString(savedSet.getId()))
                .setId(UUID.fromString(savedSet.getSetId()))
                .setName(set.getName())
                .wordCount(set.getWordCount())
                .learnedWords(savedSet.getLearnedWords())
                .build();
    }

    public static SavedSetSummaryResponseDto toSavedSetSummaryResponseDto(SavedSetResponse savedSet, VocabularySetDetailResponse set){
        return SavedSetSummaryResponseDto.builder()
                .id(UUID.fromString(savedSet.getId()))
                .setId(UUID.fromString(savedSet.getSetId()))
                .setName(set.getName())
                .wordCount(set.getWordCount())
                .learnedWords(savedSet.getLearnedWords())
                .build();
    }
}
