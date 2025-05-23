package com.nitb.apigateway.dto.Test.Test.request;

import com.nitb.apigateway.dto.Test.Part.request.CreatePartRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Parts are required.")
    @Size(min = 1, message = "The test must have at least 1 part.")
    List<@Valid CreatePartRequestDto> parts;
}
