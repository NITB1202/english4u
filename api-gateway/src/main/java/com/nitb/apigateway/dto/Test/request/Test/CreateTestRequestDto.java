package com.nitb.apigateway.dto.Test.request.Test;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTestRequestDto {
    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Minutes are required.")
    private Integer minutes;

    private String topic;
}
