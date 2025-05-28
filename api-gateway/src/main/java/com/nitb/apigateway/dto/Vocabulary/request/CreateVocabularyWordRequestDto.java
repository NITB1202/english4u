package com.nitb.apigateway.dto.Vocabulary.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVocabularyWordRequestDto {
    @NotBlank(message = "Word is required.")
    private String word;

    @NotBlank(message = "Pronunciation is required.")
    private String pronunciation;

    @NotBlank(message = "Translation is required.")
    private String translation;

    @NotBlank(message = "Example is required.")
    private String example;

    private String imageUrl;
}
