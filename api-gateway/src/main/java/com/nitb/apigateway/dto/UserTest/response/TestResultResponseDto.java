package com.nitb.apigateway.dto.UserTest.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestResultResponseDto {
    private int correctAnswers;

    private int incorrectAnswers;

    private int emptyAnswers;

    private int score;

    private float accuracy;

    private List<ResultDetailResponseDto> details;
}
