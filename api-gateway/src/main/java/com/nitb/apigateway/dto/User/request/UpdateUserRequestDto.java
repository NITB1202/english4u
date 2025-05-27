package com.nitb.apigateway.dto.User.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto {
    @NotEmpty(message = "Name is required.")
    private String name;
}
