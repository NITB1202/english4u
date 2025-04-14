package com.nitb.uservocabularyservice.service;

import com.nitb.common.exceptions.NotFoundException;
import com.nitb.uservocabularyservice.entity.CachedSet;
import com.nitb.uservocabularyservice.grpc.*;
import com.nitb.uservocabularyservice.repository.CachedSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CachedSetService {
    private final CachedSetRepository cachedSetRepository;
    private int CACHED_SIZE = 10;

    public CachedSet createCachedSet(CreateCachedSetRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UUID setId = UUID.fromString(request.getSetId());

        CachedSet cachedSet = cachedSetRepository.findByUserIdAndSetId(userId, setId);

        // If set is already cached
        if (cachedSet != null) {
            int learnedWords = Math.max(cachedSet.getLearnedWords(), request.getLearnedWords());
            cachedSet.setLearnedWords(learnedWords);
            cachedSet.setLastAccess(LocalDateTime.now());
            return cachedSet;
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

    public CachedSet getCachedSetById(GetCachedSetByIdRequest request) {
        return cachedSetRepository.findById(UUID.fromString(request.getId())).orElseThrow(() -> new NotFoundException("Cached set not found."));
    }

    public List<CachedSet> getAllCachedSets(GetAllCachedSetsRequest request) {
        return cachedSetRepository.findByUserIdOrderByLastAccessDesc(UUID.fromString(request.getUserId()));
    }

    public CachedSet updateCachedSet(UpdateCachedSetRequest request) {
        CachedSet set = cachedSetRepository.findById(UUID.fromString(request.getId())).orElseThrow(
                ()-> new NotFoundException("Cached set not found.")
        );

        set.setLearnedWords(Math.max(request.getLearnedWords(), set.getLearnedWords()));
        set.setLastAccess(LocalDateTime.now());

        return cachedSetRepository.save(set);
    }

    public void deleteCachedSet(DeleteCachedSetRequest request) {
        UUID id = UUID.fromString(request.getId());

        if(!cachedSetRepository.existsById(id)) {
            throw new NotFoundException("Cached set not found.");
        }

        cachedSetRepository.deleteById(id);
    }
}
