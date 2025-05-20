package com.nitb.apigateway.dto.Test.response.Part;

import com.nitb.apigateway.dto.Test.response.Question.QuestionSummaryResponseDto;
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

    private String content;

    private List<QuestionSummaryResponseDto> questions;
}
