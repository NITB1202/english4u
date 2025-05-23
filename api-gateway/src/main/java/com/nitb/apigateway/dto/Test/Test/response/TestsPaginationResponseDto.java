package com.nitb.apigateway.dto.Test.Test.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestsPaginationResponseDto {
    private List<TestSummaryResponseDto> tests;

    private Long totalItems;

    private Integer totalPages;
}
