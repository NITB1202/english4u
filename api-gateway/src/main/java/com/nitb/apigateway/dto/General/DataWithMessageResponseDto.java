package com.nitb.apigateway.dto.General;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataWithMessageResponseDto {
    private String message;

    private Object data;
}
