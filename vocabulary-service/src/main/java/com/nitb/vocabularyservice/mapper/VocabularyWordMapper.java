package com.nitb.vocabularyservice.mapper;

import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;

public class VocabularyWordMapper {
    private VocabularyWordMapper() {}

    public static VocabularyWordResponse toVocabularyWordResponse(VocabularyWord word) {
        return VocabularyWordResponse.newBuilder()
                .setId(word.getId().toString())
                .setPosition(word.getPosition())
                .setWord(word.getWord())
                .setPronunciation(word.getPronunciation())
                .setTranslation(word.getTranslation())
                .setExample(word.getExample())
                .build();
    }

    public static VocabularyWordsResponse toVocabularyWordsResponse(List<VocabularyWord> words) {
        List<VocabularyWordResponse> wordResponses = words.stream()
                .map(VocabularyWordMapper::toVocabularyWordResponse)
                .toList();

        return VocabularyWordsResponse.newBuilder()
                .addAllWords(wordResponses)
                .build();
    }

    public static VocabularyWordDetailResponse toVocabularyWordDetailResponse(VocabularyWord word) {
        return VocabularyWordDetailResponse.newBuilder()
                .setId(word.getId().toString())
                .setPosition(word.getPosition())
                .setWord(word.getWord())
                .setPronunciation(word.getPronunciation())
                .setTranslation(word.getTranslation())
                .setExample(word.getExample())
                .setImageUrl(word.getImageUrl())
                .build();
    }

    public static VocabularyWordsPaginationResponse toVocabularyWordsPaginationResponse(Page<VocabularyWord> words) {
        List<VocabularyWordDetailResponse> wordResponses = words.stream()
                .map(VocabularyWordMapper::toVocabularyWordDetailResponse)
                .toList();

        return VocabularyWordsPaginationResponse.newBuilder()
                .addAllWords(wordResponses)
                .setTotalItems(words.getTotalElements())
                .setTotalPage(words.getTotalPages())
                .build();
    }
}
