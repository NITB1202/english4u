package com.nitb.common.mappers;

import com.nitb.common.enums.AnswerState;

public class AnswerStateMapper {
    private AnswerStateMapper() {}

    public static AnswerState toEnum(com.nitb.usertestservice.grpc.AnswerState state) {
        return switch (state) {
            case CORRECT -> AnswerState.CORRECT;
            case INCORRECT -> AnswerState.INCORRECT;
            default -> AnswerState.EMPTY;
        };
    }

    public static com.nitb.usertestservice.grpc.AnswerState toGrpcEnum(AnswerState state) {
        return switch (state) {
            case CORRECT -> com.nitb.usertestservice.grpc.AnswerState.CORRECT;
            case INCORRECT -> com.nitb.usertestservice.grpc.AnswerState.INCORRECT;
            default -> com.nitb.usertestservice.grpc.AnswerState.EMPTY;
        };
    }
}
