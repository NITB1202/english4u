package com.nitb.apigateway.dto.Test.Test.response;

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

    private Integer version;

    private Integer questionCount;

    private Integer minutes;

    private String topic;
}
