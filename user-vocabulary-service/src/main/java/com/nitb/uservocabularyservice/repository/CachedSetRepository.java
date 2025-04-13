package com.nitb.uservocabularyservice.repository;

import com.nitb.uservocabularyservice.entity.CachedSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CachedSetRepository extends JpaRepository<CachedSet, UUID> {
    CachedSet findByUserIdAndSetId(UUID userId, UUID setId);
    List<CachedSet> findByUserIdOrderByLastAccessDesc(UUID userId);
}