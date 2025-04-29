package com.nitb.apigateway.dto.Test.request.Question;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddQuestionsToPartRequestDto {
    @NotNull(message = "Part id is required.")
    private UUID partId;

    @NotNull(message = "Questions are required.")
    @Size(min = 1, message = "The list must contain at least one item.")
    private List<CreateQuestionRequestDto> questions;
}
