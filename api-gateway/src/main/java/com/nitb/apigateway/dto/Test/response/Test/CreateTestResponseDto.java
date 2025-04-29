package com.nitb.apigateway.dto.Test.response.Test;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTestResponseDto {
    private UUID id;

    private UUID createdBy;

    private LocalDateTime createdAt;

    private String name;

    private Integer minutes;

    private String topic;
}
