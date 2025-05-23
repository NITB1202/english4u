package com.nitb.apigateway.dto.Test.Question.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionSummaryResponseDto {
    private UUID id;

    private Integer position;

    private String content;

    private List<String> answers;
}
