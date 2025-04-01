package com.nitb.apigateway.dto.Vocabulary.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVocabularySetRequestDto {
    @NotNull(message = "User id is required.")
    private UUID userId;

    @NotBlank(message = "Name is required.")
    @Size(min = 3, max = 20, message = "Name must be between 3 and 20 characters.")
    private String name;

    private List<CreateVocabularyWordRequestDto> words;
}
