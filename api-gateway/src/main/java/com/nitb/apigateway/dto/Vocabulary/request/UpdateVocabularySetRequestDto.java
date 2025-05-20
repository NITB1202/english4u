package com.nitb.apigateway.dto.Vocabulary.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVocabularySetRequestDto {
    @NotNull(message = "Words are required.")
    @Size(min = 1, message = "The set must contain at least 1 word.")
    private List<CreateVocabularyWordRequestDto> words;
}
