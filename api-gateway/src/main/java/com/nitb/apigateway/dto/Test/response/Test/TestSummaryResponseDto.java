package com.nitb.apigateway.dto.Test.response.Test;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestSummaryResponseDto {
    private UUID id;

    private String name;

    private Integer questionCount;

    private Integer minutes;

    private String topic;
}
