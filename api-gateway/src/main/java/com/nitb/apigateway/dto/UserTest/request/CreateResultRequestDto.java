package com.nitb.apigateway.dto.UserTest.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateResultRequestDto {
    private UUID userId;

    private UUID testId;

    private long secondsSpent;

    private int score;

    private float accuracy;
}
