package com.nitb.vocabularyservice.repository;

import com.nitb.vocabularyservice.dto.VocabularySetStatisticProjection;
import com.nitb.vocabularyservice.entity.VocabularySet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface VocabularySetRepository extends JpaRepository<VocabularySet, UUID> {
    boolean existsByNameAndIsDeletedFalse(String name);
    Page<VocabularySet> findAllByIsDeletedFalse(Pageable pageable);
    Page<VocabularySet> findAllByIsDeletedTrue(Pageable pageable);
    Page<VocabularySet> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);
    @Query(value = """
    SELECT TO_CHAR(created_at, :pattern) AS time, COUNT(*) AS count
    FROM vocabulary_sets
    WHERE created_by = :userId
        AND is_deleted = false
        AND created_at BETWEEN :from AND :to
    GROUP BY TO_CHAR(created_at, :pattern)
    ORDER BY TO_CHAR(created_at, :pattern)
    """, nativeQuery = true)
    List<VocabularySetStatisticProjection> countByPattern(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("pattern") String pattern
    );
}