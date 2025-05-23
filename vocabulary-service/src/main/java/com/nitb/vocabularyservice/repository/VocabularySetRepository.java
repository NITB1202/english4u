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
    int countByName(String name);
    Page<VocabularySet> findAllByIsDeletedFalse(Pageable pageable);
    Page<VocabularySet> findAllByIsDeletedTrue(Pageable pageable);
    Page<VocabularySet> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);
    Page<VocabularySet> findByNameContainingIgnoreCaseAndIsDeletedTrue(String name, Pageable pageable);
    VocabularySet findByNameAndIsDeletedFalse(String name);
    @Query(value = """
        SELECT TO_CHAR(created_at, 'IYYY-IW') AS time,
               COUNT(*) AS count
        FROM vocabulary_sets
        WHERE created_by = :userId
          AND is_deleted = false
          AND created_at BETWEEN :from AND :to
        GROUP BY TO_CHAR(created_at, 'IYYY-IW')
        ORDER BY TO_CHAR(created_at, 'IYYY-IW')
        """, nativeQuery = true)
    List<VocabularySetStatisticProjection> countByWeek(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    @Query(value = """
        SELECT TO_CHAR(created_at, 'YYYY-MM') AS time,
               COUNT(*) AS count
        FROM vocabulary_sets
        WHERE created_by = :userId
          AND is_deleted = false
          AND created_at BETWEEN :from AND :to
        GROUP BY TO_CHAR(created_at, 'YYYY-MM')
        ORDER BY TO_CHAR(created_at, 'YYYY-MM')
        """, nativeQuery = true)
    List<VocabularySetStatisticProjection> countByMonth(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    @Query(value = """
        SELECT TO_CHAR(created_at, 'YYYY') AS time,
               COUNT(*) AS count
        FROM vocabulary_sets
        WHERE created_by = :userId
          AND is_deleted = false
          AND created_at BETWEEN :from AND :to
        GROUP BY TO_CHAR(created_at, 'YYYY')
        ORDER BY TO_CHAR(created_at, 'YYYY')
        """, nativeQuery = true)
    List<VocabularySetStatisticProjection> countByYear(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    @Query(value = """
        SELECT MAX(version)
        FROM vocabulary_sets
        WHERE name = :name
    """, nativeQuery = true)
    int getLatestVersion(@Param("name") String name);
}