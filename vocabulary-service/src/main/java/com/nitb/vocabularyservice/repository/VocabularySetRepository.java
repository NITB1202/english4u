package com.nitb.vocabularyservice.repository;

import com.nitb.vocabularyservice.entity.VocabularySet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VocabularySetRepository extends JpaRepository<VocabularySet, UUID> {
    boolean existsByName(String name);
    Page<VocabularySet> findAllByIsDeletedFalse(Pageable pageable);
    Page<VocabularySet> findAllByIsDeletedTrue(Pageable pageable);
    Page<VocabularySet> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);
    int countByUpdatedBy(UUID userId);
}