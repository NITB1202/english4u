package com.nitb.usertestservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "test_id", nullable = false)
    private UUID testId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "submit_time", nullable = false)
    private LocalDateTime submitTime;

    @Column(name = "seconds_spent", nullable = false)
    private Long secondsSpent;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "accuracy", nullable = false)
    private Float accuracy;
}
