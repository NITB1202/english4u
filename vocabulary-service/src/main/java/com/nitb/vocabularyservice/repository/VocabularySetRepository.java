package com.nitb.vocabularyservice.repository;

import com.nitb.vocabularyservice.entity.VocabularySet;
import com.nitb.vocabularyservice.grpc.VocabularySetStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VocabularySetRepository extends JpaRepository<VocabularySet, UUID> {
    boolean existsByName(String name);
    Page<VocabularySet> findAllByIsDeletedFalse(Pageable pageable);
    Page<VocabularySet> findAllByIsDeletedTrue(Pageable pageable);
    Page<VocabularySet> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);
    @Query("""
    SELECT new com.nitb.vocabularyservice.grpc.VocabularySetStatistic(
        TO_CHAR(v.createdAt, 'IYYY-IW'),
        COUNT(v)
    )
    FROM VocabularySet v
    WHERE v.createdBy = :userId
      AND v.isDeleted = false
      AND v.createdAt BETWEEN :from AND :to
    GROUP BY TO_CHAR(v.createdAt, 'IYYY-IW')
    ORDER BY TO_CHAR(v.createdAt, 'IYYY-IW')
""")
    List<VocabularySetStatistic> countPublishedByWeek(UUID userId, LocalDate from, LocalDate to);

    @Query("""
    SELECT new com.nitb.vocabularyservice.grpc.VocabularySetStatistic(
        TO_CHAR(v.createdAt, 'YYYY-MM'),
        COUNT(v)
    )
    FROM VocabularySet v
    WHERE v.createdBy = :userId
      AND v.isDeleted = false
      AND v.createdAt BETWEEN :from AND :to
    GROUP BY TO_CHAR(v.createdAt, 'YYYY-MM')
    ORDER BY TO_CHAR(v.createdAt, 'YYYY-MM')
""")
    List<VocabularySetStatistic> countPublishedByMonth(UUID userId, LocalDate from, LocalDate to);

    @Query("""
    SELECT new com.nitb.vocabularyservice.grpc.VocabularySetStatistic(
        TO_CHAR(v.createdAt, 'YYYY'),
        COUNT(v)
    )
    FROM VocabularySet v
    WHERE v.createdBy = :userId
      AND v.isDeleted = false
      AND v.createdAt BETWEEN :from AND :to
    GROUP BY TO_CHAR(v.createdAt, 'YYYY')
    ORDER BY TO_CHAR(v.createdAt, 'YYYY')
""")
    List<VocabularySetStatistic> countPublishedByYear(UUID userId, LocalDate from, LocalDate to);


}