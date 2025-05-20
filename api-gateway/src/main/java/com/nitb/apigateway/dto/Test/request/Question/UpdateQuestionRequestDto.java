package com.nitb.apigateway.dto.Test.request.Question;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateQuestionRequestDto {
    private String content;

    private List<String> answers;

    private Character correctAnswer;

    private String explanation;
}
