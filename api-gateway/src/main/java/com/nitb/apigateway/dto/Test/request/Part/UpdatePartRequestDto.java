package com.nitb.apigateway.dto.Test.request.Part;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePartRequestDto {
    @NotBlank(message = "Content is required.")
    private String content;
}
