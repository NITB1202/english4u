package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.UserTest.request.CreateResultDetailRequestDto;
import com.nitb.common.mappers.AnswerStateMapper;
import com.nitb.usertestservice.grpc.CreateResultDetailRequest;

public class ResultDetailMapper {
    private ResultDetailMapper() {}

    public static CreateResultDetailRequest toCreateResultDetailRequest(CreateResultDetailRequestDto detail) {
        return CreateResultDetailRequest.newBuilder()
                .setQuestionId(detail.getQuestionId().toString())
                .setUserAnswer(Character.toString(detail.getUserAnswer()))
                .setState(AnswerStateMapper.toEnum(detail.getState()))
                .build();
    }
}
