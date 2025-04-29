package com.nitb.apigateway.dto.Test.response.Part;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartSummaryResponseDto {
    private UUID id;

    private Integer position;

    private Integer questionCount;
}
