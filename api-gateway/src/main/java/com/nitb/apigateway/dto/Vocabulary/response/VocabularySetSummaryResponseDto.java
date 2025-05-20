package com.nitb.apigateway.dto.Vocabulary.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularySetSummaryResponseDto {
    private UUID id;

    private String name;

    private int version;

    private Integer wordCount;
}
