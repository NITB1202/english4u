package com.nitb.uservocabularyservice.mapper;

import com.nitb.uservocabularyservice.entity.SavedSet;
import com.nitb.uservocabularyservice.grpc.SavedSetResponse;
import com.nitb.uservocabularyservice.grpc.SavedSetSummaryResponse;
import com.nitb.uservocabularyservice.grpc.SavedSetsPaginationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

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

    public static SavedSetSummaryResponse toSavedSetSummaryResponse(SavedSet savedSet) {
        return SavedSetSummaryResponse.newBuilder()
                .setId(savedSet.getId().toString())
                .setSetId(savedSet.getSetId().toString())
                .setLearnedWords(savedSet.getLearnedWords())
                .build();
    }

    public static SavedSetsPaginationResponse toSavedSetsPaginationResponse( Page<SavedSet> sets) {
        List<SavedSetSummaryResponse> setsResponse = sets.getContent().stream()
                .map(SavedSetMapper::toSavedSetSummaryResponse)
                .toList();

        return SavedSetsPaginationResponse.newBuilder()
                .addAllSets(setsResponse)
                .setTotalItems(sets.getTotalElements())
                .setTotalPages(sets.getTotalPages())
                .build();
    }
}
