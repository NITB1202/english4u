package com.nitb.vocabularyservice.mapper;

import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.VocabularySetResponse;

public class VocabularySetMapper {
    private VocabularySetMapper() {}

    public static VocabularySetResponse toVocabularySetResponse(VocabularySet vocabularySet) {
        return VocabularySetResponse.newBuilder()
                .setId(vocabularySet.getId().toString())
                .setName(vocabularySet.getName())
                .setWordCount(vocabularySet.getWordCount())
                .setCreateAt(vocabularySet.getCreatedBy().toString())
                .setCreateAt(vocabularySet.getCreatedAt().toString())
                .setUpdatedBy(vocabularySet.getUpdatedBy().toString())
                .setUpdateAt(vocabularySet.getUpdatedAt().toString())
                .setIsDeleted(vocabularySet.getIsDeleted())
                .build();
    }
}
