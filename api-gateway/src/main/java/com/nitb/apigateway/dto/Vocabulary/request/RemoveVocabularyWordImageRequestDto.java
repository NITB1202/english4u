package com.nitb.apigateway.dto.Vocabulary.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoveVocabularyWordImageRequestDto {
    @NotEmpty(message = "Url is required.")
    private String url;
}
