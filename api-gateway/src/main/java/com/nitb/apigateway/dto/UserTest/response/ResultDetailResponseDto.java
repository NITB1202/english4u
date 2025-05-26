package com.nitb.apigateway.dto.UserTest.response;

import com.nitb.usertestservice.grpc.AnswerState;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultDetailResponseDto {
    private UUID id;

    private UUID questionId;

    private Integer position;

    private Character userAnswer;

    private AnswerState state;
}
