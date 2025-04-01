package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.vocabularyservice.grpc.CreateVocabularyWordRequest;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class VocabularyMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private VocabularyMapper() {}

    public static CreateVocabularyWordRequest toCreateVocabularyWordRequest(CreateVocabularyWordRequestDto word) {
        return CreateVocabularyWordRequest.newBuilder()
                .setWord(word.getWord())
                .setPronun(word.getPronunciation())
                .setTrans(word.getTranslation())
                .setEx(word.getExample())
                .build();
    }

    public static VocabularySetResponseDto toVocabularySetResponseDto(VocabularySetResponse set) {
        return VocabularySetResponseDto.builder()
                .id(UUID.fromString(set.getId()))
                .createdBy(UUID.fromString(set.getCreatedBy()))
                .createAt(LocalDateTime.parse(set.getCreateAt(), formatter))
                .name(set.getName())
                .wordCount(set.getWordCount())
                .updatedBy(UUID.fromString(set.getUpdatedBy()))
                .updatedAt(LocalDateTime.parse(set.getUpdatedBy(), formatter))
                .isDeleted(set.getIsDeleted())
                .build();
    }

    public static VocabularyWordResponseDto toVocabularyWordResponseDto(VocabularyWordResponse word) {
        return VocabularyWordResponseDto.builder()
                .id(UUID.fromString(word.getId()))
                .setId(UUID.fromString(word.getId()))
                .position(word.getPosition())
                .word(word.getWord())
                .pronunciation(word.getPronun())
                .translation(word.getTrans())
                .example(word.getEx())
                .imageUrl(word.getImageUrl())
                .build();
    }
}
