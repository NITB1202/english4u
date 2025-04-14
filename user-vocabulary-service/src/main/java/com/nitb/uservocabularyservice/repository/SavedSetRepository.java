package com.nitb.uservocabularyservice.repository;

import com.nitb.uservocabularyservice.entity.SavedSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SavedSetRepository extends JpaRepository<SavedSet, UUID> {
    boolean existsByUserIdAndSetId(UUID userId, UUID setId);
    Page<SavedSet> findByUserId(UUID userId, Pageable pageable);
    List<SavedSet> findByUserId(UUID userId);
}