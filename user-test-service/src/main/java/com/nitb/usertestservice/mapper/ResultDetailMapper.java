package com.nitb.usertestservice.mapper;

import com.nitb.common.mappers.AnswerStateMapper;
import com.nitb.usertestservice.entity.ResultDetail;
import com.nitb.usertestservice.grpc.AnswerState;
import com.nitb.usertestservice.grpc.ResultDetailResponse;

public class ResultDetailMapper {
    private ResultDetailMapper() {}

    public static ResultDetailResponse toResultDetailResponse(ResultDetail resultDetail) {
        AnswerState state = AnswerStateMapper.toEnum(resultDetail.getState());

        return ResultDetailResponse.newBuilder()
                .setId(resultDetail.getId().toString())
                .setQuestionId(resultDetail.getQuestionId().toString())
                .setUserAnswer(resultDetail.getUserAnswer())
                .setState(state)
                .build();
    }
}
