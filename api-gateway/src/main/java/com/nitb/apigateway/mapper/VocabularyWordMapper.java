package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.vocabularyservice.grpc.CreateVocabularyWordRequest;
import com.nitb.vocabularyservice.grpc.VocabularyWordResponse;

import java.util.UUID;

public class VocabularyWordMapper {
    private VocabularyWordMapper() {}

    public static CreateVocabularyWordRequest toCreateVocabularyWordRequest(CreateVocabularyWordRequestDto word) {
        return CreateVocabularyWordRequest.newBuilder()
                .setWord(word.getWord())
                .setPronun(word.getPronunciation())
                .setTrans(word.getTranslation())
                .setEx(word.getExample())
                .build();
    }

    public static VocabularyWordResponseDto toVocabularyWordResponseDto(VocabularyWordResponse word) {
        return VocabularyWordResponseDto.builder()
                .id(UUID.fromString(word.getId()))
                .setId(UUID.fromString(word.getSetId()))
                .position(word.getPosition())
                .word(word.getWord())
                .pronunciation(word.getPronun())
                .translation(word.getTrans())
                .example(word.getEx())
                .imageUrl(word.getImageUrl())
                .build();
    }
}
