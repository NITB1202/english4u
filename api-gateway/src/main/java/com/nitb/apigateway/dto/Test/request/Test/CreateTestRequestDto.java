package com.nitb.apigateway.dto.Test.request.Test;

import com.nitb.apigateway.dto.Test.request.Part.CreatePartRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

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

    @NotNull(message = "Parts are required.")
    @Size(min = 1, message = "The test must have at least 1 part.")
    List<CreatePartRequestDto> parts;
}
