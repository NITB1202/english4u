package com.nitb.apigateway.dto.Test.Test.request;

import com.nitb.apigateway.dto.Test.Part.request.CreatePartRequestDto;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTestRequestDto {
    @Positive(message = "Minutes must be positive.")
    private Integer minutes;

    List<CreatePartRequestDto> parts;
}
