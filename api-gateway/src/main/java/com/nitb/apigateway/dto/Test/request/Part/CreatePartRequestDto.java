package com.nitb.apigateway.dto.Test.request.Part;

import com.nitb.apigateway.dto.Test.request.Question.CreateQuestionRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePartRequestDto {
    private String content;

    @NotNull(message = "Questions are required.")
    @Size(min = 1, message = "Part must have at least 1 question.")
    private List<@Valid CreateQuestionRequestDto> questions;
}
