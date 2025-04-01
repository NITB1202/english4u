package com.nitb.vocabularyservice.mapper;

import com.nitb.vocabularyservice.entity.VocabularyWord;
import com.nitb.vocabularyservice.grpc.VocabularyWordResponse;

import java.util.Optional;

public class VocabularyWordMapper {
    private VocabularyWordMapper() {}

    public static VocabularyWordResponse toVocabularyWordResponse(VocabularyWord word) {
        return VocabularyWordResponse.newBuilder()
                .setId(word.getId().toString())
                .setSetId(word.getSetId().toString())
                .setPosition(word.getPosition())
                .setWord(word.getWord())
                .setPronun(word.getPronunciation())
                .setTrans(word.getTranslation())
                .setEx(word.getExample())
                .setImageUrl(Optional.ofNullable(word.getImageUrl()).orElse(""))
                .build();
    }
}
