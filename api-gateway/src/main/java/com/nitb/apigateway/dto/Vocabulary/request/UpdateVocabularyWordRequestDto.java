package com.nitb.apigateway.dto.Vocabulary.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVocabularyWordRequestDto {
    private String word;

    private String pronunciation;

    private String translation;

    private String example;
}
