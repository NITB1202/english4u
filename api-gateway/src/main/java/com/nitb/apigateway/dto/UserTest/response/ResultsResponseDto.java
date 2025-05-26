package com.nitb.apigateway.dto.UserTest.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultsResponseDto {
    private List<ResultSummaryResponseDto> results;

    private long totalItems;

    private int totalPages;
}
