package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.UserTest.request.CreateResultDetailRequestDto;
import com.nitb.apigateway.dto.UserTest.response.ResultDetailResponseDto;
import com.nitb.common.mappers.AnswerStateMapper;
import com.nitb.usertestservice.grpc.CreateResultDetailRequest;
import com.nitb.usertestservice.grpc.ResultDetailResponse;

import java.util.UUID;

public class ResultDetailMapper {
    private ResultDetailMapper() {}

    public static CreateResultDetailRequest toCreateResultDetailRequest(CreateResultDetailRequestDto detail) {
        String userAnswer = detail.getUserAnswer() != null ? detail.getUserAnswer() : "";

        return CreateResultDetailRequest.newBuilder()
                .setQuestionId(detail.getQuestionId().toString())
                .setUserAnswer(userAnswer)
                .setState(AnswerStateMapper.toGrpcEnum(detail.getState()))
                .build();
    }

    public static ResultDetailResponseDto toResultDetailResponseDto(int position, ResultDetailResponse detail) {
        return ResultDetailResponseDto.builder()
                .id(UUID.fromString(detail.getId()))
                .questionId(UUID.fromString(detail.getQuestionId()))
                .position(position)
                .userAnswer(detail.getUserAnswer())
                .state(AnswerStateMapper.toEnum(detail.getState()))
                .build();
    }
}
