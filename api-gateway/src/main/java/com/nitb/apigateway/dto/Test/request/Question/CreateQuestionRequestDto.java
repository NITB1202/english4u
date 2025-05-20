package com.nitb.apigateway.dto.Test.request.Question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQuestionRequestDto {
    @NotBlank(message = "Content is required.")
    private String content;

    @NotNull(message = "Answers are required.")
    private List<String> answers;

    @NotNull(message = "Correct answer is required.")
    private Character correctAnswer;

    private String explanation;
}
