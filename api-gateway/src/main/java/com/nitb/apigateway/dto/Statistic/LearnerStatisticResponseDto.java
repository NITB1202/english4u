package com.nitb.apigateway.dto.Statistic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LearnerStatisticResponseDto {
    private UUID userId;

    private String name;

    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime joinAt;

    private long totalTestsTaken;

    private double avgScore;

    private boolean isLocked;
}
