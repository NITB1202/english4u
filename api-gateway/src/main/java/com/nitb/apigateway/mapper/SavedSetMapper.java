package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetDetailResponseDto;
import com.nitb.apigateway.dto.UserVocabulary.response.SavedSetResponseDto;
import com.nitb.uservocabularyservice.grpc.SavedSetResponse;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;

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

    public static SavedSetDetailResponseDto toSavedSetDetailResponseDto(SavedSetResponse savedSet, VocabularySetResponse set){
        return SavedSetDetailResponseDto.builder()
                .id(UUID.fromString(savedSet.getId()))
                .userId(UUID.fromString(savedSet.getUserId()))
                .setId(UUID.fromString(savedSet.getSetId()))
                .setName(set.getName())
                .wordCount(set.getWordCount())
                .learnedWords(savedSet.getLearnedWords())
                .lastAccess(LocalDateTime.parse(savedSet.getLastAccess()))
                .build();
    }
}
