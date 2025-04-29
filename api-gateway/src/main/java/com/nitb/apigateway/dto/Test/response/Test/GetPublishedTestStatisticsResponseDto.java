package com.nitb.apigateway.dto.Test.response.Test;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetPublishedTestStatisticsResponseDto {
    List<TestStatisticResponseDto> statistics;
}
