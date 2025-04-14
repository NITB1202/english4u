package com.nitb.apigateway.dto.UserVocabulary.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSavedSetRequestDto {
    @NotNull(message = "Learned words are required.")
    @PositiveOrZero(message = "Learned words must be greater than or equal to 0.")
    private Integer learnedWords;
}
