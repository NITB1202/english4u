package com.nitb.apigateway.dto.UserVocabulary.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedSetResponseDto {
    private UUID id;

    private UUID userId;

    private UUID setId;

    private Integer learnedWords;

    private LocalDateTime lastAccess;
}
