package com.nitb.apigateway.dto.Vocabulary.request;

import jakarta.validation.constraints.NotBlank;
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
public class CreateVocabularySetRequestDto {
    @NotBlank(message = "Name is required.")
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters.")
    private String name;

    @NotNull(message = "Words are required.")
    @Size(min = 1, message = "The set must contain at least 1 word.")
    private List<CreateVocabularyWordRequestDto> words;
}
