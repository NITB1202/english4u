package com.nitb.apigateway.dto.Test.response.Part;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartResponseDto {
    private UUID id;

    private Integer position;

    private String content;

    private Integer questionCount;
}
