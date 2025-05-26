package com.nitb.apigateway.dto.UserTest.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavedResultResponseDto {
    private UUID id;
}
