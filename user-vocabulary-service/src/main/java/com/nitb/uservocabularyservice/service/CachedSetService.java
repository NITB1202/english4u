package com.nitb.uservocabularyservice.service;

import com.nitb.uservocabularyservice.entity.CachedSet;
import com.nitb.uservocabularyservice.grpc.CreateCachedSetRequest;
import com.nitb.uservocabularyservice.grpc.DeleteCachedSetIfExistsRequest;
import com.nitb.uservocabularyservice.grpc.GetAllCachedSetsRequest;

import java.util.List;

public interface CachedSetService {
    CachedSet createCachedSet(CreateCachedSetRequest request);
    List<CachedSet> getAllCachedSets(GetAllCachedSetsRequest request);
    boolean deleteCachedSetIfExists(DeleteCachedSetIfExistsRequest request);
}
