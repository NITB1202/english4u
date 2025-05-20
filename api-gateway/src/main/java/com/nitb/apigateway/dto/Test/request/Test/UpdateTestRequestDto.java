package com.nitb.apigateway.dto.Test.request.Test;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTestRequestDto {
    private String name;

    @Positive(message = "Minutes must be positive.")
    private Integer minutes;

    private String topic;
}
