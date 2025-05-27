package com.nitb.apigateway.dto.UserVocabulary.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CachedSetSummaryResponseDto {
    private UUID setId;

    private String setName;

    private Integer wordCount;

    private Integer learnedWords;

    private Boolean isDeleted;
}
