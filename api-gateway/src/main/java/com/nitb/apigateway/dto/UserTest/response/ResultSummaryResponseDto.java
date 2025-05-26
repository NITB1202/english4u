package com.nitb.apigateway.dto.UserTest.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultSummaryResponseDto {
    private UUID id;

    private String testName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate submitDate;

    private int score;

    private int secondsSpent;
}
