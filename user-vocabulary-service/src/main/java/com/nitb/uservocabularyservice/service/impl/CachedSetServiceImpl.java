package com.nitb.uservocabularyservice.service.impl;

import com.nitb.uservocabularyservice.entity.CachedSet;
import com.nitb.uservocabularyservice.grpc.*;
import com.nitb.uservocabularyservice.repository.CachedSetRepository;
import com.nitb.uservocabularyservice.service.CachedSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CachedSetServiceImpl implements CachedSetService {
    private final CachedSetRepository cachedSetRepository;
    private int CACHED_SIZE = 10;

    @Override
    public CachedSet createCachedSet(CreateCachedSetRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID setId = UUID.fromString(request.getSetId());

        CachedSet cachedSet = cachedSetRepository.findByUserIdAndSetId(userId, setId);

        // If set is already cached
        if (cachedSet != null) {
            int learnedWords = Math.max(cachedSet.getLearnedWords(), request.getLearnedWords());
            cachedSet.setLearnedWords(learnedWords);
            cachedSet.setLastAccess(LocalDateTime.now());
            return cachedSetRepository.save(cachedSet);
        }

        //If not, delete saved sets until we have enough slots for the new one.
        List<CachedSet> sets = cachedSetRepository.findByUserIdOrderByLastAccessDesc(userId);
        int size = sets.size();

        while(size >= CACHED_SIZE) {
            cachedSetRepository.delete(sets.get(--size));
        }

        CachedSet set = CachedSet.builder()
                .userId(userId)
                .setId(setId)
                .learnedWords(request.getLearnedWords())
                .lastAccess(LocalDateTime.now())
                .build();

        return cachedSetRepository.save(set);
    }

    @Override
    public List<CachedSet> getAllCachedSets(GetAllCachedSetsRequest request) {
        return cachedSetRepository.findByUserIdOrderByLastAccessDesc(UUID.fromString(request.getUserId()));
    }

    @Override
    public boolean deleteCachedSetIfExists(DeleteCachedSetIfExistsRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID setId = UUID.fromString(request.getSetId());

        CachedSet set = cachedSetRepository.findByUserIdAndSetId(userId, setId);

        if (set != null) {
            cachedSetRepository.delete(set);
            return true;
        }

        return false;
    }
}
