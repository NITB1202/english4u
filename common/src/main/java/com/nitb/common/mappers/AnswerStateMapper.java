package com.nitb.common.mappers;

import com.nitb.common.enums.AnswerState;

public class AnswerStateMapper {
    private AnswerStateMapper() {}

    public static AnswerState toGrpcEnum(com.nitb.usertestservice.grpc.AnswerState state) {
        return switch (state) {
            case CORRECT -> AnswerState.CORRECT;
            case INCORRECT -> AnswerState.INCORRECT;
            default -> AnswerState.EMPTY;
        };
    }
}
