package com.nitb.apigateway.dto.Test.response.Test;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteTestResponseDto {
    private UUID id;

    private UUID updatedBy;

    private LocalDateTime updatedAt;

    private Boolean isDeleted;
}
