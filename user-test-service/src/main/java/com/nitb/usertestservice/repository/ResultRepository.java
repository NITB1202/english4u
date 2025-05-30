package com.nitb.usertestservice.repository;

import com.nitb.usertestservice.dto.ResultStatisticProjection;
import com.nitb.usertestservice.entity.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result, UUID> {
    Page<Result> findByUserId(UUID userId, Pageable pageable);
    @Query(value = """
    SELECT TO_CHAR(submit_time, 'IYYY-IW') AS time,
           COUNT(*) AS resultCount,
           ROUND(AVG(seconds_spent), 2) AS avgSecondsSpent,
           ROUND(AVG(accuracy), 2) AS avgAccuracy
    FROM results
    WHERE user_id = :userId
      AND submit_time BETWEEN :from AND :to
    GROUP BY TO_CHAR(submit_time, 'IYYY-IW')
    ORDER BY TO_CHAR(submit_time, 'IYYY-IW')
    """, nativeQuery = true)
    List<ResultStatisticProjection> getStatsByWeek(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    @Query(value = """
    SELECT TO_CHAR(submit_time, 'YYYY-MM') AS time,
           COUNT(*) AS resultCount,
           ROUND(AVG(seconds_spent), 2) AS avgSecondsSpent,
           ROUND(AVG(accuracy), 2) AS avgAccuracy
    FROM results
    WHERE user_id = :userId
      AND submit_time BETWEEN :from AND :to
    GROUP BY TO_CHAR(submit_time, 'YYYY-MM')
    ORDER BY TO_CHAR(submit_time, 'YYYY-MM')
    """, nativeQuery = true)
    List<ResultStatisticProjection> getStatsByMonth(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    @Query(value = """
    SELECT TO_CHAR(submit_time, 'YYYY') AS time,
           COUNT(*) AS resultCount,
           ROUND(AVG(seconds_spent), 2) AS avgSecondsSpent,
           ROUND(AVG(accuracy), 2) AS avgAccuracy
    FROM results
    WHERE user_id = :userId
      AND submit_time BETWEEN :from AND :to
    GROUP BY TO_CHAR(submit_time, 'YYYY')
    ORDER BY TO_CHAR(submit_time, 'YYYY')
    """, nativeQuery = true)
    List<ResultStatisticProjection> getStatsByYear(
            @Param("userId") UUID userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    long countByUserId(UUID userId);
    long sumScoreByUserId(UUID userId);
}