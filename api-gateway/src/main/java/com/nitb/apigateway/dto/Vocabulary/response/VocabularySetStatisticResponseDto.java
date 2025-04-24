package com.nitb.apigateway.dto.Vocabulary.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularySetStatisticResponseDto {
    private int publishedCount;
}
