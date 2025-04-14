package com.nitb.apigateway.dto.UserVocabulary.response;

import com.nitb.apigateway.dto.Vocabulary.response.VocabularySetResponseDto;
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
public class CachedSetDetailResponseDto {
    private UUID id;

    private UUID userId;

    private VocabularySetResponseDto set;

    private Integer learnedWords;

    private LocalDateTime lastAccess;
}
