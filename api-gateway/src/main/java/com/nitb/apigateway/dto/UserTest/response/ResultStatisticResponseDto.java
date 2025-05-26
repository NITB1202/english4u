package com.nitb.apigateway.dto.UserTest.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultStatisticResponseDto {
    private String time;

    private long resultCount;

    private long avgSecondsSpent;

    private double avgAccuracy;
}
