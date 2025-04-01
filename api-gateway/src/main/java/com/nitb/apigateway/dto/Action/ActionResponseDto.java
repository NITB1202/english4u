package com.nitb.apigateway.dto.Action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionResponseDto {
    boolean success;

    String message;
}
