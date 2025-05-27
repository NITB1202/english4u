package com.nitb.apigateway.dto.User.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserResponseDto {
    private UUID id;

    private String name;
}
