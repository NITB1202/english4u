package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.common.grpc.ActionResponse;

public class ActionMapper {
    private ActionMapper() {}

    public static ActionResponseDto toResponseDto(ActionResponse action) {
        return ActionResponseDto.builder()
                .success(action.getSuccess())
                .message(action.getMessage())
                .build();
    }
}
