package com.nitb.apigateway.dto.UserTest.request;

import com.nitb.common.enums.AnswerState;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateResultDetailRequestDto {
    private UUID questionId;

    private char userAnswer;

    private AnswerState state;
}
