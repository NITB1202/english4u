package com.nitb.apigateway.dto.Auth.Auth.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenerateAccessTokenResponseDto {
    private String accessToken;
}
