package com.nitb.usertestservice.repository;

import com.nitb.usertestservice.dto.ResultStatisticProjection;
import com.nitb.usertestservice.entity.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result, UUID> {
    Page<Result> findByUserId(UUID userId, Pageable pageable);
    @Query(value = """
    SELECT TO_CHAR(submit_date, 'IYYY-IW') AS time,
           COUNT(*) AS resultCount,
           AVG(seconds_spent) AS avgSecondsSpent,
           AVG(accuracy) AS avgAccuracy
    FROM results
    WHERE user_id = :userId
      AND submit_date BETWEEN :from AND :to
    GROUP BY TO_CHAR(submit_date, 'IYYY-IW')
    ORDER BY TO_CHAR(submit_date, 'IYYY-IW')
    """, nativeQuery = true)
    List<ResultStatisticProjection> getStatsByWeek(
            @Param("userId") UUID userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
    @Query(value = """
    SELECT TO_CHAR(submit_date, 'YYYY-MM') AS time,
           COUNT(*) AS resultCount,
           AVG(seconds_spent) AS avgSecondsSpent,
           AVG(accuracy) AS avgAccuracy
    FROM results
    WHERE user_id = :userId
      AND submit_date BETWEEN :from AND :to
    GROUP BY TO_CHAR(submit_date, 'IYYY-IW')
    ORDER BY TO_CHAR(submit_date, 'IYYY-IW')
    """, nativeQuery = true)
    List<ResultStatisticProjection> getStatsByMonth(
            @Param("userId") UUID userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
    @Query(value = """
    SELECT TO_CHAR(submit_date, 'YYYY') AS time,
           COUNT(*) AS resultCount,
           AVG(seconds_spent) AS avgSecondsSpent,
           AVG(accuracy) AS avgAccuracy
    FROM results
    WHERE user_id = :userId
      AND submit_date BETWEEN :from AND :to
    GROUP BY TO_CHAR(submit_date, 'IYYY-IW')
    ORDER BY TO_CHAR(submit_date, 'IYYY-IW')
    """, nativeQuery = true)
    List<ResultStatisticProjection> getStatsByYear(
            @Param("userId") UUID userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}