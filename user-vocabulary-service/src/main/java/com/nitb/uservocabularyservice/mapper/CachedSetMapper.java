package com.nitb.uservocabularyservice.mapper;

import com.nitb.uservocabularyservice.entity.CachedSet;
import com.nitb.uservocabularyservice.grpc.CachedSetResponse;

public class CachedSetMapper {
    private CachedSetMapper() {}

    public static CachedSetResponse toCachedSetResponse(CachedSet cachedSet) {
        return CachedSetResponse.newBuilder()
                .setId(cachedSet.getId().toString())
                .setUserId(cachedSet.getUserId().toString())
                .setSetId(cachedSet.getSetId().toString())
                .setLearnedWords(cachedSet.getLearnedWords())
                .setLastAccess(cachedSet.getLastAccess().toString())
                .build();
    }
}
