package com.nitb.apigateway.dto.Test.response.Question;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponseDto {
    private UUID id;

    private Integer position;

    private String content;

    private List<String> answers;

    private Character correctAnswer;

    private String explanation;
}
