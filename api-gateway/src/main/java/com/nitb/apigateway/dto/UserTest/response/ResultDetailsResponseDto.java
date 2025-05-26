package com.nitb.apigateway.dto.UserTest.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultDetailsResponseDto {
    private UUID id;

    private String testName;

    private int correctAnswers;

    private int incorrectAnswers;

    private int emptyAnswers;

    private int score;

    private float accuracy;

    private int secondsSpent;

    private List<ResultDetailResponseDto> details;
}
