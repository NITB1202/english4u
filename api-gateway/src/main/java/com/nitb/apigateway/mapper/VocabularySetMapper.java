package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetSummaryResponse;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;

import java.util.UUID;

public class VocabularySetMapper {
    private VocabularySetMapper() {}

    public static VocabularySetSummaryResponse toVocabularySetResponseDto(VocabularySetResponse set) {
        return VocabularySetSummaryResponse.builder()
                .id(UUID.fromString(set.getId()))
                .name(set.getName())
                .wordCount(set.getWordCount())
                .build();
    }
}
