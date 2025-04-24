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
public class VocabularyWordDetailResponseDto {
    private UUID id;

    private Integer position;

    private String word;

    private String pronunciation;

    private String translation;

    private String example;

    private String imageUrl;
}
