package com.nitb.uservocabularyservice.mapper;

import com.nitb.uservocabularyservice.entity.SavedSet;
import com.nitb.uservocabularyservice.grpc.SavedSetResponse;

public class SavedSetMapper {
    private SavedSetMapper() {}

    public static SavedSetResponse toSavedSetResponse(SavedSet savedSet) {
        return SavedSetResponse.newBuilder()
                .setId(savedSet.getId().toString())
                .setUserId(savedSet.getUserId().toString())
                .setSetId(savedSet.getSetId().toString())
                .setLearnedWords(savedSet.getLearnedWords())
                .setLastAccess(savedSet.getLastAccess().toString())
                .build();
    }
}
