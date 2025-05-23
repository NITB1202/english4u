package com.nitb.apigateway.dto.Test.Part.response;

import com.nitb.apigateway.dto.Test.Question.response.QuestionSummaryResponseDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartDetailResponseDto {
    private UUID id;

    private Integer position;

    private String content;

    private Integer questionCount;

    private List<QuestionSummaryResponseDto> questions;
}
