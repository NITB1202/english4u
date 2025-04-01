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
public class UpdateVocabularySetRequestDto {
    @NotBlank(message = "Name is required.")
    private String name;
}
