package com.nitb.vocabularyservice.mapper;

import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.*;
import org.springframework.data.domain.Page;

import java.util.List;

public class VocabularyWordMapper {
    private VocabularyWordMapper() {}

    public static VocabularyWordSummaryResponse toVocabularyWordSummaryResponse(VocabularyWord word) {
        return VocabularyWordSummaryResponse.newBuilder()
                .setId(word.getId().toString())
                .setWord(word.getWord())
                .build();
    }

    public static VocabularyWordsResponse toVocabularyWordsResponse(List<VocabularyWord> words) {
        List<VocabularyWordSummaryResponse> summaries = words.stream()
                .map(VocabularyWordMapper::toVocabularyWordSummaryResponse)
                .toList();

        return VocabularyWordsResponse.newBuilder()
                .addAllWords(summaries)
                .build();
    }

    public static VocabularyWordResponse toVocabularyWordResponse(VocabularyWord word) {
        String imageUrl = word.getImageUrl() != null ? word.getImageUrl() : "";

        return VocabularyWordResponse.newBuilder()
                .setId(word.getId().toString())
                .setPosition(word.getPosition())
                .setWord(word.getWord())
                .setPronunciation(word.getPronunciation())
                .setTranslation(word.getTranslation())
                .setExample(word.getExample())
                .setImageUrl(imageUrl)
                .build();
    }

    public static VocabularyWordsPaginationResponse toVocabularyWordsPaginationResponse(Page<VocabularyWord> words) {
        List<VocabularyWordResponse> wordResponses = words.stream()
                .map(VocabularyWordMapper::toVocabularyWordResponse)
                .toList();

        return VocabularyWordsPaginationResponse.newBuilder()
                .addAllWords(wordResponses)
                .setTotalItems(words.getTotalElements())
                .setTotalPage(words.getTotalPages())
                .build();
    }
}
