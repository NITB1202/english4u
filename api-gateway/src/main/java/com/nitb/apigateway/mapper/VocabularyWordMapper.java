package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Vocabulary.request.CreateVocabularyWordRequestDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordSummaryResponseDto;
import com.nitb.apigateway.dto.Vocabulary.response.VocabularyWordsPaginationResponseDto;
import com.nitb.vocabularyservice.grpc.CreateVocabularyWordRequest;
import com.nitb.vocabularyservice.grpc.VocabularyWordResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordSummaryResponse;
import com.nitb.vocabularyservice.grpc.VocabularyWordsPaginationResponse;

import java.util.List;
import java.util.UUID;

public class VocabularyWordMapper {
    private VocabularyWordMapper() {}

    public static CreateVocabularyWordRequest toCreateVocabularyWordRequest(CreateVocabularyWordRequestDto word) {
        return CreateVocabularyWordRequest.newBuilder()
                .setWord(word.getWord())
                .setPronunciation(word.getPronunciation())
                .setTranslation(word.getTranslation())
                .setExample(word.getExample())
                .build();
    }

    public static VocabularyWordResponseDto toVocabularyWordResponseDto(VocabularyWordResponse word) {
        return VocabularyWordResponseDto.builder()
                .id(UUID.fromString(word.getId()))
                .position(word.getPosition())
                .word(word.getWord())
                .pronunciation(word.getPronunciation())
                .translation(word.getTranslation())
                .example(word.getExample())
                .imageUrl(word.getImageUrl())
                .build();
    }

    public static VocabularyWordsPaginationResponseDto toVocabularyWordsPaginationResponseDto(VocabularyWordsPaginationResponse pagination) {
        List<VocabularyWordResponseDto> words = pagination.getWordsList().stream()
                .map(VocabularyWordMapper::toVocabularyWordResponseDto)
                .toList();

        return VocabularyWordsPaginationResponseDto.builder()
                .words(words)
                .totalItems(pagination.getTotalItems())
                .totalPages(pagination.getTotalPage())
                .build();
    }

    public static VocabularyWordSummaryResponseDto toVocabularyWordSummaryResponseDto(VocabularyWordSummaryResponse word) {
        return VocabularyWordSummaryResponseDto.builder()
                .id(UUID.fromString(word.getId()))
                .word(word.getWord())
                .build();
    }
}
