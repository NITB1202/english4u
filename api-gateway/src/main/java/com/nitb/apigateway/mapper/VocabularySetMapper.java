package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetResponseDto;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class VocabularySetMapper {
    private VocabularySetMapper() {}

    public static VocabularySetResponseDto toVocabularySetResponseDto(VocabularySetResponse set) {
        return VocabularySetResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .createdBy(UUID.fromString(set.getCreatedBy()))
                .createAt(LocalDateTime.parse(set.getCreateAt()))
                .name(set.getName())
                .wordCount(set.getWordCount())
                .updatedBy(UUID.fromString(set.getUpdatedBy()))
                .updatedAt(LocalDateTime.parse(set.getUpdateAt()))
                .isDeleted(set.getIsDeleted())
                .build();
    }
}
