package com.nitb.testservice.repository;

import com.nitb.testservice.dto.TestStatisticProjection;
import com.nitb.testservice.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
    boolean existsByNameAndIsDeletedFalse(String name);
    int countByName(String name);
    Page<Test> findByIsDeletedFalse(Pageable pageable);
    Page<Test> findByIsDeletedTrue(Pageable pageable);
    Page<Test> findByNameContainingIgnoreCaseAndIsDeletedFalse(String name, Pageable pageable);
    Page<Test> findByNameContainingIgnoreCaseAndIsDeletedTrue(String name, Pageable pageable);
    @Query(value = """
    SELECT TO_CHAR(create_at, 'IYYY-IW') AS time,
           COUNT(*) AS testCount,
           SUM(completed_users) AS completedUsers
    FROM tests
    WHERE created_by = :userId
      AND (is_deleted IS NULL OR is_deleted = false)
      AND create_at BETWEEN :from AND :to
    GROUP BY TO_CHAR(create_at, 'IYYY-IW')
    ORDER BY TO_CHAR(create_at, 'IYYY-IW')
    """, nativeQuery = true)
    List<TestStatisticProjection> getStatsByWeek(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    @Query(value = """
        SELECT TO_CHAR(create_at, 'YYYY-MM') AS time,
               COUNT(*) AS testCount,
               SUM(completed_users) AS completedUsers
        FROM tests
        WHERE created_by = :userId
          AND (is_deleted IS NULL OR is_deleted = false)
          AND create_at BETWEEN :from AND :to
        GROUP BY TO_CHAR(create_at, 'YYYY-MM')
        ORDER BY TO_CHAR(create_at, 'YYYY-MM')
        """, nativeQuery = true)
    List<TestStatisticProjection> getStatsByMonth(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    @Query(value = """
        SELECT TO_CHAR(create_at, 'YYYY') AS time,
               COUNT(*) AS testCount,
               SUM(completed_users) AS completedUsers
        FROM tests
        WHERE created_by = :userId
          AND (is_deleted IS NULL OR is_deleted = false)
          AND create_at BETWEEN :from AND :to
        GROUP BY TO_CHAR(create_at, 'YYYY')
        ORDER BY TO_CHAR(create_at, 'YYYY')
        """, nativeQuery = true)
    List<TestStatisticProjection> getStatsByYear(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    @Query("SELECT MAX(t.version) FROM Test t WHERE t.name = :name")
    Integer findMaxVersion(@Param("name") String name);
    Test findByNameAndIsDeletedFalse(String name);
    Long countByCreatedByAndIsDeletedFalse(UUID createdBy);
}